package com.example.myapplication.data

import com.example.myapplication.R

// Data class defining Menu Item properties
data class MenuItem(
    val id: String,
    val name: String,
    val currency: String,
    val price: Double,
    val description: String,
    val category: String,
    val meat: List<String>,
    val side: List<String>,
    val drink: String,
    val allergens: List<String>,
    var rated_by: Int = 0,
    var rating_total: Double = 0.0,
    val photo: String = "android.resource://com.example.myapplication/" + R.drawable.food
)