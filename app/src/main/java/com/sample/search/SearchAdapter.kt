package com.sample.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sample.core.data.local.CharacterEntity
import com.sample.databinding.SearchListItemBinding
import com.sample.core.utils.applyDefaults

class SearchAdapter(
    private val onItemClicked: (CharacterEntity, ImageView) -> Unit
) : ListAdapter<CharacterEntity, SearchAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(
        private val binding: SearchListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: CharacterEntity) {
            binding.root.setOnClickListener { onItemClicked(entity, binding.image) }

            binding.name.text = entity.name

            Glide.with(binding.root)
                .load(entity.imageUrl)
                .applyDefaults()
                .into(binding.image)
        }
    }

    companion object {

        private val DIFF_UTIL = object : DiffUtil.ItemCallback<CharacterEntity>() {
            override fun areItemsTheSame(
                oldItem: CharacterEntity,
                newItem: CharacterEntity
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: CharacterEntity,
                newItem: CharacterEntity
            ): Boolean = oldItem == newItem
        }
    }
}