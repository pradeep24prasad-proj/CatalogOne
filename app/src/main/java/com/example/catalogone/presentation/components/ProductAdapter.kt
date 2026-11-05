package com.example.catalogone.presentation.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.catalogone.databinding.ItemBrowseAllCardBinding
import com.example.catalogone.databinding.ItemProductCardBinding
import com.example.catalogone.domain.model.Product
import java.util.Locale

class ProductAdapter(
    private val onProductClick: (Int) -> Unit,
    private val onBrowseAllClick: (() -> Unit)? = null
) : ListAdapter<Product, RecyclerView.ViewHolder>(ProductDiffCallback) {

    companion object {
        private const val VIEW_TYPE_PRODUCT = 0
        private const val VIEW_TYPE_BROWSE_ALL = 1
    }

    override fun getItemViewType(position: Int): Int {
        val product = getItem(position)
        return if (product.id == -1) VIEW_TYPE_BROWSE_ALL else VIEW_TYPE_PRODUCT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_BROWSE_ALL) {
            val binding = ItemBrowseAllCardBinding.inflate(inflater, parent, false)
            BrowseAllViewHolder(binding, onBrowseAllClick)
        } else {
            val binding = ItemProductCardBinding.inflate(inflater, parent, false)
            ProductViewHolder(binding, onProductClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductViewHolder) {
            holder.bind(getItem(position))
        } else if (holder is BrowseAllViewHolder) {
            holder.bind()
        }
    }

    class ProductViewHolder(
        private val binding: ItemProductCardBinding,
        private val onProductClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            val context = binding.root.context
            binding.tvCategoryBadge.text = context.getString(com.example.catalogone.R.string.top_rated)
            binding.tvProductTitle.text = product.title
            binding.tvProductPrice.text = "$${String.format(Locale.US, "%.2f", product.price)}"
            binding.tvProductRating.text = "★ ${product.rating.rate} (${product.rating.count} reviews)"
            binding.ivProductImage.load(product.image)

            binding.cardProduct.setOnClickListener {
                onProductClick(product.id)
            }
        }
    }

    class BrowseAllViewHolder(
        private val binding: ItemBrowseAllCardBinding,
        private val onBrowseAllClick: (() -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.cardBrowseAll.setOnClickListener {
                onBrowseAllClick?.invoke()
            }
        }
    }

    private object ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem == newItem
    }
}
