package com.sample.features.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import com.sample.core.actions.Actions
import com.sample.features.search.databinding.SearchActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by viewModels()

    private lateinit var binding: SearchActivityBinding
    private lateinit var searchView: SearchView
    private lateinit var searchMenuItem: MenuItem

    private var charactersJob: Job? = null
    private var savedQueryState: CharSequence? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        savedQueryState = savedInstanceState?.getCharSequence(KEY_SAVED_QUERY_STATE)

        val searchAdapter = SearchAdapter(viewModel::onItemClicked)
        setupRecyclerView(searchAdapter)
        setupViewModelEvents(searchAdapter)
    }

    private fun setupRecyclerView(searchAdapter: SearchAdapter) = with(binding.recyclerView) {
        adapter = searchAdapter
        addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.grid_spacing)))
    }

    private fun setupViewModelEvents(searchAdapter: SearchAdapter) = with(viewModel) {
        charactersUpdates
            .onEach { characters ->
                charactersJob?.cancel()
                charactersJob = lifecycleScope.launch {
                    characters.collectLatest {
                        searchAdapter.submitData(it)
                    }
                }
            }.launchIn(lifecycleScope)

        openDetailsScreen
            .onEach { characterEntity ->
                val activity = this@SearchActivity
                startActivity(Actions.openDetailsIntent(activity, characterEntity.id))
            }.launchIn(lifecycleScope)

        scrollToTop
            .onEach { binding.recyclerView.smoothScrollToPosition(0) }
            .launchIn(lifecycleScope)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        searchMenuItem = menu.findItem(R.id.action_search)
        searchView = searchMenuItem.actionView as SearchView

        setupSearchView(searchMenuItem)

        return super.onCreateOptionsMenu(menu)
    }

    private fun setupSearchView(searchMenuItem: MenuItem) {
        with(searchView) {
            queryHint = getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.onQueryTextChanged(newText)
                    return true
                }
            })
        }

        val hasSavedQueryState = !savedQueryState.isNullOrEmpty()
        if (hasSavedQueryState) {
            searchMenuItem.expandActionView()
            searchView.setQuery(savedQueryState, true)
            searchView.clearFocus()
        }

        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                viewModel.onSearchExpanded()
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.onSearchCollapsed()
                return true
            }
        })

        searchView.findViewById<View>(R.id.search_close_btn).setOnClickListener {
            searchView.setQuery("", false)
            viewModel.onClearSearchClicked()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(KEY_SAVED_QUERY_STATE, searchView.query)
    }

    companion object {
        private const val KEY_SAVED_QUERY_STATE = "KEY_SAVED_QUERY_STATE"
    }
}
