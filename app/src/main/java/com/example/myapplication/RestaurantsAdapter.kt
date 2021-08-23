package com.example.myapplication

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.data.MenuItem
import kotlinx.android.synthetic.main.a_single_restaurant_row.view.*

// Adapter class for Menu Items
class RestaurantsAdapter(private val menuItems: MutableList<MenuItem>) : RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.a_single_restaurant_row, parent, false)
        return ViewHolder(view)
    }

    // Get number of menu items
    override fun getItemCount() = menuItems.size

    // Loop through menu items
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = menuItems[position]
        holder.bind(item)
    }

    // Update adapter with new items
    fun notifyChanges(oldList: MutableList<MenuItem>, newList: MutableList<MenuItem>) {
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

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        // Display info for each item on main page
        fun bind(menuItem: MenuItem) {
            itemView.menuItemName.text = menuItem.name
            itemView.menuItemPrice.text = "${menuItem.currency}${"%.2f".format(menuItem.price)}"
            itemView.menuItemRating.rating = (menuItem.rating_total / menuItem.rated_by).toFloat()

            Glide.with(view)
                .load(Uri.parse(menuItem.photo))
                .into(itemView.menuItemImage)

            // When clicked on menu item photo, go to its info (new page)
            view.menuItemImage.setOnClickListener {
                val action = FirstFragmentDirections.actionFirstFragmentToMenuItemInfo(menuItem.id)
                view.findNavController().navigate(action)
            }
        }
    }
}