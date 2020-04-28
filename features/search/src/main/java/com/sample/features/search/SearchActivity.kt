package com.sample.features.search

import android.app.ActivityOptions
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sample.core.actions.Actions
import com.sample.core.data.di.coreComponent
import com.sample.features.search.databinding.SearchActivityBinding
import com.sample.features.search.di.DaggerSearchComponent
import com.sample.features.search.di.SearchModule
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: SearchViewModel

    private lateinit var binding: SearchActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        binding = SearchActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchAdapter = SearchAdapter(viewModel::onItemClicked)
        with(binding.recyclerView) {
            adapter = searchAdapter
            addItemDecoration(SpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.grid_spacing)))
        }

        viewModel.allCharacters
            .onEach { searchAdapter.submitList(it) }
            .launchIn(lifecycleScope)

        viewModel.openDetailsScreen
            .onEach { (characterEntity, imageView) ->
                val intent = Actions.openDetailsIntent(this, characterEntity.id)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    imageView,
                    resources.getString(R.string.shared_element_name)
                )
                startActivity(intent, options.toBundle())
            }
            .launchIn(lifecycleScope)
    }

    private fun inject() {
        DaggerSearchComponent.builder()
            .searchModule(SearchModule(this))
            .coreComponent(application.coreComponent)
            .build()
            .inject(this)
    }
}
