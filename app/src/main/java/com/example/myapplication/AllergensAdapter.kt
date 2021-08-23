package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.filters_allergy.view.allergyFilterText

// Adapter for showing allergens in Menu Item Info
class AllergensAdapter (var allergens: List<String>) : RecyclerView.Adapter<AllergensAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.filters_allergy, parent, false)
        return ViewHolder(view)
    }

    // Get number of filter items
    override fun getItemCount() = allergens.size

    // Loop through filter items
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = allergens[position]
        holder.bind(item)
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        // Display each allergen
        fun bind(item: String) {
            itemView.allergyFilterText.text = item
        }
    }
}