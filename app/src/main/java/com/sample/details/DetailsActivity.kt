package com.sample.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.sample.R
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.local.CharacterEntity.Gender
import com.sample.core.data.local.CharacterEntity.Status
import com.sample.databinding.DetailsActivityBinding
import com.sample.di.Injector
import com.sample.utils.applyDefaults
import com.sample.viewmodel.DaggerViewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class DetailsActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory: DaggerViewModelFactory

    private val viewModel by viewModels<DetailsViewModel> { viewModelFactory }

    private lateinit var binding: DetailsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Injector.get().inject(this)
        super.onCreate(savedInstanceState)
        binding = DetailsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.init(characterId = intent.extras?.getInt(EXTRA_CHARACTER_ID)!!)

        viewModel.character
            .onEach { displayCharacter(it) }
            .launchIn(lifecycleScope)
    }

    private fun displayCharacter(entity: CharacterEntity) = with(entity) {
        title = name

        Glide.with(this@DetailsActivity)
            .load(imageUrl)
            .applyDefaults()
            .into(binding.image)

        binding.name.text = getString(R.string.details_name, name)
        binding.status.text = getString(
            R.string.details_status,
            when (status) {
                Status.ALIVE -> getString(R.string.status_alive)
                Status.DEAD -> getString(R.string.status_dead)
                Status.UNKNOWN -> getString(R.string.unknown)
            }
        )
        binding.species.text = getString(R.string.details_species, species)
        binding.gender.text = getString(
            R.string.details_gender,
            when (gender) {
                Gender.FEMALE -> getString(R.string.gender_female)
                Gender.MALE -> getString(R.string.gender_male)
                Gender.GENDERLESS -> getString(R.string.gender_genderless)
                Gender.UNKNOWN -> getString(R.string.unknown)
            }
        )
        binding.location.text = getString(R.string.details_location, location.name)
        binding.created.text = getString(R.string.details_created, created)

        with(binding.type) {
            text = getString(R.string.details_type, type)
            isVisible = !type.isNullOrBlank()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        private const val EXTRA_CHARACTER_ID = "EXTRA_CHARACTER_ID"

        fun createIntent(context: Context, characterId: Int) =
            Intent(context, DetailsActivity::class.java).apply {
                putExtra(EXTRA_CHARACTER_ID, characterId)
            }
    }
}