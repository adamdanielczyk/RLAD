package com.sample.search

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sample.data.local.CharacterEntity
import com.sample.databinding.SearchListItemBinding

class SearchAdapter : ListAdapter<CharacterEntity, SearchAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ViewHolder(
        private val binding: SearchListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: CharacterEntity) {
            binding.name.text = entity.name

            Glide.with(binding.root)
                .load(entity.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(ColorDrawable(Color.LTGRAY))
                .fallback(ColorDrawable(Color.GRAY))
                .error(ColorDrawable(Color.RED))
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