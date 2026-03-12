package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FiltersAllergyBinding

// Adapter for showing allergens in Menu Item Info
class AllergensAdapter (var allergens: List<String>) : RecyclerView.Adapter<AllergensAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FiltersAllergyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Get number of filter items
    override fun getItemCount() = allergens.size

    // Loop through filter items
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = allergens[position]
        holder.bind(item)
    }

    class ViewHolder(private val binding: FiltersAllergyBinding) : RecyclerView.ViewHolder(binding.root) {
        // Display each allergen
        fun bind(item: String) {
            binding.allergyFilterText.text = item
        }
    }
}