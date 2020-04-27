package com.sample.search

import android.app.ActivityOptions
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sample.R
import com.sample.databinding.SearchActivityBinding
import com.sample.details.DetailsActivity
import com.sample.di.Injector
import com.sample.utils.SpaceItemDecoration
import com.sample.viewmodel.DaggerViewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: DaggerViewModelFactory

    private val viewModel by viewModels<SearchViewModel> { viewModelFactory }

    private lateinit var binding: SearchActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Injector.get().inject(this)
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
                val intent = DetailsActivity.createIntent(this, characterEntity.id)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    imageView,
                    resources.getString(R.string.shared_element_name)
                )
                startActivity(intent, options.toBundle())
            }
            .launchIn(lifecycleScope)
    }
}
