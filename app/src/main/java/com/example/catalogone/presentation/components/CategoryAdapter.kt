package com.example.catalogone.presentation.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogone.R
import com.example.catalogone.databinding.ItemCategoryChipBinding

class CategoryAdapter(
    private val onCategoryClick: (String) -> Unit
) : ListAdapter<String, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback) {

    private var selectedCategory: String = "all"

    fun setSelectedCategory(category: String) {
        selectedCategory = category
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryChipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding, onCategoryClick) { selectedCategory }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CategoryViewHolder(
        private val binding: ItemCategoryChipBinding,
        private val onCategoryClick: (String) -> Unit,
        private val getSelectedCategory: () -> String
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: String) {
            val formattedText = if (category.lowercase() == "all") {
                "All"
            } else {
                category.split(" ").joinToString(" ") { word ->
                    word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                }
            }

            binding.tvCategoryChip.text = formattedText

            val isSelected = category.equals(getSelectedCategory(), ignoreCase = true)
            if (isSelected) {
                binding.tvCategoryChip.setBackgroundResource(R.drawable.bg_category_chip_active)
                binding.tvCategoryChip.setTextColor(0xFFFFFFFF.toInt())
            } else {
                binding.tvCategoryChip.setBackgroundResource(R.drawable.bg_category_chip_inactive)
                binding.tvCategoryChip.setTextColor(0xFF49454F.toInt())
            }

            binding.tvCategoryChip.setOnClickListener {
                onCategoryClick(category)
            }
        }
    }

    private object CategoryDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
    }
}
