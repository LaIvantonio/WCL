package com.example.wclchat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wclchat.databinding.ItemQuestBinding

class QuestsAdapter(private val onClick: (Attraction) -> Unit) :
    ListAdapter<Attraction, QuestsAdapter.QuestViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestViewHolder {
        val binding = ItemQuestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: QuestViewHolder, position: Int) {
        val attraction = getItem(position)
        holder.bind(attraction)
        holder.itemView.setOnClickListener {
            onClick(attraction)
        }
    }

    class QuestViewHolder(
        private val binding: ItemQuestBinding,
        private val onClick: (Attraction) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(attraction: Attraction) {
            binding.textViewName.text = attraction.name
            binding.root.setOnClickListener { onClick(attraction) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Attraction>() {
        override fun areItemsTheSame(oldItem: Attraction, newItem: Attraction): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Attraction, newItem: Attraction): Boolean {
            return oldItem.name == newItem.name
        }
    }
}
