package com.sample.features.details

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.sample.core.actions.EXTRA_CHARACTER_ID
import com.sample.core.data.di.coreComponent
import com.sample.core.data.local.CharacterEntity
import com.sample.core.data.local.CharacterEntity.Gender
import com.sample.core.data.local.CharacterEntity.Status
import com.sample.core.utils.applyDefaults
import com.sample.features.details.databinding.DetailsActivityBinding
import com.sample.features.details.di.DaggerDetailsComponent
import com.sample.features.details.di.DetailsModule
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class DetailsActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: DetailsViewModel

    private lateinit var binding: DetailsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        binding = DetailsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.character
            .onEach { displayCharacter(it) }
            .launchIn(lifecycleScope)
    }

    private fun inject() {
        DaggerDetailsComponent.builder()
            .characterId(intent.extras?.getInt(EXTRA_CHARACTER_ID)!!)
            .detailsModule(DetailsModule(this))
            .coreComponent(application.coreComponent)
            .build()
            .inject(this)
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
}