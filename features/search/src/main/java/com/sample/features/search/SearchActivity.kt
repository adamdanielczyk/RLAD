package com.sample.features.search

import android.app.ActivityOptions
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import com.sample.core.actions.Actions
import com.sample.core.data.di.coreComponent
import com.sample.features.search.databinding.SearchActivityBinding
import com.sample.features.search.di.DaggerSearchComponent
import com.sample.features.search.di.SearchModule
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: SearchViewModel

    private lateinit var binding: SearchActivityBinding
    private lateinit var searchView: SearchView
    private lateinit var searchMenuItem: MenuItem

    private var charactersJob: Job? = null
    private var savedQueryState: CharSequence? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        binding = SearchActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedQueryState = savedInstanceState?.getCharSequence(KEY_SAVED_QUERY_STATE)

        val searchAdapter = SearchAdapter(viewModel::onItemClicked)
        setupRecyclerView(searchAdapter)
        setupViewModelEvents(searchAdapter)
    }

    private fun inject() {
        DaggerSearchComponent.builder()
            .searchModule(SearchModule(this))
            .coreComponent(application.coreComponent)
            .build()
            .inject(this)
    }

    private fun setupRecyclerView(searchAdapter: SearchAdapter) = with(binding.recyclerView) {
        adapter = searchAdapter
        addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.grid_spacing)))
    }

    private fun setupViewModelEvents(searchAdapter: SearchAdapter) = with(viewModel) {
        charactersUpdates
            .onEach { characters ->
                charactersJob?.cancel()
                charactersJob = characters.onEach {
                    searchAdapter.submitList(it)
                }.launchIn(lifecycleScope)
            }.launchIn(lifecycleScope)

        openDetailsScreen
            .onEach { (characterEntity, imageView) ->
                val activity = this@SearchActivity
                val intent = Actions.openDetailsIntent(activity, characterEntity.id)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    activity,
                    imageView,
                    resources.getString(R.string.shared_element_name)
                )
                startActivity(intent, options.toBundle())
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
