package com.example.myapplication

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.data.MenuItem
import com.example.myapplication.databinding.ASingleRestaurantRowBinding

// Adapter class for Menu Items
class RestaurantsAdapter(private val menuItems: MutableList<MenuItem>) : RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ASingleRestaurantRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Get number of menu items
    override fun getItemCount() = menuItems.size

    // Loop through menu items
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = menuItems[position]
        holder.bind(item)
    }

    // Update adapter with new items
    fun notifyChanges(oldList: List<MenuItem>, newList: List<MenuItem>) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].id == newList[newItemPosition].id
            }
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
            override fun getOldListSize() = oldList.size
            override fun getNewListSize() = newList.size
        })
        diff.dispatchUpdatesTo(this)
    }

    class ViewHolder(private val binding: ASingleRestaurantRowBinding) : RecyclerView.ViewHolder(binding.root) {
        // Display info for each item on main page
        @SuppressLint("SetTextI18n")
        fun bind(menuItem: MenuItem) {
            binding.menuItemName.text = menuItem.name
            binding.menuItemPrice.text = "${menuItem.currency}${"%.2f".format(menuItem.price)}"
            binding.menuItemRating.rating = (menuItem.rating_total / menuItem.rated_by).toFloat()

            Glide.with(binding.root)
                .load(Uri.parse(menuItem.photo))
                .into(binding.menuItemImage)

            // When clicked on menu item photo, go to its info (new page)
            binding.menuItemImage.setOnClickListener {
                val action = FirstFragmentDirections.actionFirstFragmentToMenuItemInfo(menuItem.id)
                binding.root.findNavController().navigate(action)
            }
        }
    }
}