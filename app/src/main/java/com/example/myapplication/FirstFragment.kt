package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.MenuItemData
import androidx.navigation.findNavController
import com.example.myapplication.data.MenuItem
import com.example.myapplication.databinding.FragmentFirstBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Main screen of the application listing Menu Items
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gson = Gson()

        // Save current menuItems to shared pref menu_items to restore on reload
        fun saveMenuItems(items: MutableList<MenuItem>) {
            val menuItems = gson.toJson(items)
            requireActivity().getSharedPreferences("menu_items", Context.MODE_PRIVATE).edit().apply {
                putString("menu_items", menuItems)
            }.apply()
        }

        // Retrieve menuItems from shared pref menu_items on reload
        // If no menuItems are saved return original MenuItemData
        fun getMyMenuItems(): MutableList<MenuItem> {
            val myMenuItems = requireActivity().getSharedPreferences("menu_items", Context.MODE_PRIVATE).getString("menu_items", "default value")
            if (myMenuItems != "default value") {
                val listType = object : TypeToken<MutableList<MenuItem>>() { }.type
                val obj = gson.fromJson<MutableList<MenuItem>>(myMenuItems, listType)
                return obj
            }
            else {
                return MenuItemData().allMenuItems
            }
        }

        // Get menu items and feed them to adapter
        val menuItems = getMyMenuItems()
        val mAdapter = RestaurantsAdapter(menuItems)

        // Add a menu item to the adapter and save the menu items
        fun addItem(newItem: MenuItem) {
            // Check whether the ID exists to avoid duplicate items
            if (menuItems[menuItems.size - 1].id != newItem.id) {
                menuItems.add(newItem)
                saveMenuItems(menuItems)

                MenuItemData().allMenuItems = menuItems

                mAdapter.notifyDataSetChanged()
            }
        }

        // Load new item from shared pref new_item and convert to MenuItem object
        fun loadNewItem() {
            val newItemText = requireActivity().getSharedPreferences("new_item", Context.MODE_PRIVATE).getString("new_item", "default value")

            if(newItemText.toString() != "default value"){
                val obj = gson.fromJson(newItemText, MenuItem::class.java)
                addItem(obj)
            }
        }

        // Render menu items
        binding.restaurantsRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }

        // On click of + button go to Second Fragment (add new item to menu)
        binding.addNewItemToMenu.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        // Check whether there have been any items saved in shared pref new_item and load them/it
        if(requireActivity().getSharedPreferences("new_item", Context.MODE_PRIVATE).getString("new_item", "default value") != "default value"){
            loadNewItem()
        }

        var selectedCategoryFilter = ""
        val selectedMeatFilters = mutableListOf<String>()
        val selectedSideFilters = mutableListOf<String>()
        var selectedDrinkFilter = ""
        val selectedAllergenFilter = mutableListOf<String>()

        fun setCategoryFilters(){
            val categoryFilters = listOf<CardView>(binding.cardView1, binding.cardView2, binding.cardView3, binding.cardView4, binding.cardView5)
            val categoryFilterTexts = listOf<TextView>(binding.categoryFilterText1, binding.categoryFilterText2, binding.categoryFilterText3, binding.categoryFilterText4, binding.categoryFilterText5)

            val meatFilterIcons = listOf<ImageView>(binding.meatFilterIcon1, binding.meatFilterIcon2, binding.meatFilterIcon3, binding.meatFilterIcon4,
                binding.meatFilterIcon5, binding.meatFilterIcon6, binding.meatFilterIcon7)
            val meatFilterUnclickedIconSources = listOf(R.drawable.beef_black, R.drawable.chicken_black, R.drawable.fish_black,
                R.drawable.pork_black, R.drawable.seafood_black, R.drawable.vegan_black, R.drawable.vegetarian_black)
            val sideFilterIcons = listOf<ImageView>(binding.sideFilterIcon1, binding.sideFilterIcon2, binding.sideFilterIcon3, binding.sideFilterIcon4,
                binding.sideFilterIcon5, binding.sideFilterIcon6, binding.sideFilterIcon7)
            val sideFilterUnclickedIconSources = listOf(R.drawable.bread_black, R.drawable.dumplings_black, R.drawable.mushrooms_black,
                R.drawable.pasta_black, R.drawable.potatoes_black, R.drawable.rice_black, R.drawable.sauce_black)
            val drinkFilterIcons = listOf<ImageView>(binding.drinkFilterIcon1, binding.drinkFilterIcon2, binding.drinkFilterIcon3, binding.drinkFilterIcon4,
                binding.drinkFilterIcon5, binding.drinkFilterIcon6, binding.drinkFilterIcon7)
            val drinkFilterUnclickedIconSources = listOf(R.drawable.alcohol_black, R.drawable.beer_black, R.drawable.cocktail_black,
                R.drawable.hot_black, R.drawable.mocktail_black, R.drawable.soft_black, R.drawable.wine_black)

            for(i in 0..4){
                categoryFilters[i].setOnClickListener {
                    val allMenuItems = getMyMenuItems()
                    val oldList = menuItems.toMutableList()
                    var filteredList: MutableList<MenuItem>
                    val preFilteredList: MutableList<MenuItem>

                    if (categoryFilters[i].cardBackgroundColor.getColorForState(
                            categoryFilters[i].drawableState, Color.parseColor("#DADADA")) != Color.GRAY) {
                        // Change background color when filter clicked and unclick others
                        categoryFilters.forEach{it.setCardBackgroundColor(Color.parseColor("#DADADA"))}
                        categoryFilters[i].setCardBackgroundColor(Color.GRAY)
                        selectedCategoryFilter = categoryFilterTexts[i].text.toString()

                        if(selectedCategoryFilter == "Drink"){
                            for(i in 0..6){
                                // Unclick meat and side filters
                                meatFilterIcons[i].setImageResource(meatFilterUnclickedIconSources[i])
                                sideFilterIcons[i].setImageResource(sideFilterUnclickedIconSources[i])
                            }
                        }
                        else{
                            for(i in 0..6){
                                // Unclick drink filters
                                drinkFilterIcons[i].setImageResource(drinkFilterUnclickedIconSources[i])
                            }
                            selectedDrinkFilter = ""
                        }
                        if(selectedMeatFilters.isNotEmpty() && selectedSideFilters.isNotEmpty()) {
                            val prePreFilteredList = allMenuItems.filter { (selectedMeatFilters.intersect(it.meat)).isNotEmpty() } as MutableList<MenuItem>
                            preFilteredList = prePreFilteredList.filter { (selectedSideFilters.intersect(it.side)).isNotEmpty() } as MutableList<MenuItem>
                        }
                        else if(selectedMeatFilters.isNotEmpty()){
                            preFilteredList = allMenuItems.filter { (selectedMeatFilters.intersect(it.meat)).isNotEmpty() } as MutableList<MenuItem>
                        }
                        else if(selectedSideFilters.isNotEmpty()){
                            preFilteredList = allMenuItems.filter { (selectedSideFilters.intersect(it.side)).isNotEmpty() } as MutableList<MenuItem>
                        }
                        else if(selectedDrinkFilter != ""){
                            preFilteredList = allMenuItems.filter { it.drink == selectedDrinkFilter } as MutableList
                        }
                        else{
                            preFilteredList = allMenuItems.toMutableList()
                        }
                        filteredList = preFilteredList.filter { it.category == selectedCategoryFilter} as MutableList
                    }
                    else {
                        // Change background color when filter unclicked
                        categoryFilters[i].setCardBackgroundColor(Color.parseColor("#DADADA"))
                        selectedCategoryFilter = ""
                        if(selectedMeatFilters.isNotEmpty() && selectedSideFilters.isNotEmpty()){
                            val preFilteredList = allMenuItems.filter { (selectedMeatFilters.intersect(it.meat)).isNotEmpty() } as MutableList<MenuItem>
                            filteredList = preFilteredList.filter { (selectedSideFilters.intersect(it.side)).isNotEmpty() } as MutableList<MenuItem>
                        }
                        else if(selectedMeatFilters.isNotEmpty()) {
                            filteredList = allMenuItems.filter { (selectedMeatFilters.intersect(it.meat)).isNotEmpty() } as MutableList<MenuItem>
                        }
                        else if(selectedSideFilters.isNotEmpty()){
                            filteredList = allMenuItems.filter { (selectedSideFilters.intersect(it.side)).isNotEmpty() } as MutableList<MenuItem>
                        }
                        else if(selectedDrinkFilter != ""){
                            filteredList = allMenuItems.filter { it.drink == selectedDrinkFilter } as MutableList
                        }
                        else{
                            filteredList = allMenuItems
                        }
                    }
                    if(selectedAllergenFilter.isNotEmpty()){
                        filteredList = filteredList.filter { (selectedAllergenFilter.intersect(it.allergens)).isEmpty() } as MutableList
                    }
                    // Update menuItems with applied filter and reload adapter
                    menuItems.clear()
                    menuItems.addAll(filteredList)
                    mAdapter.notifyChanges(oldList, menuItems)
                }
            }
        }

        fun setMeatFilters(){
            val meatFilters = listOf<CardView>(binding.cardView21, binding.cardView22, binding.cardView23, binding.cardView24, binding.cardView25, binding.cardView26, binding.cardView27)
            val meatFilterIcons = listOf<ImageView>(binding.meatFilterIcon1, binding.meatFilterIcon2, binding.meatFilterIcon3, binding.meatFilterIcon4,
                binding.meatFilterIcon5, binding.meatFilterIcon6, binding.meatFilterIcon7)
            val meatFilterIconSources = listOf(R.drawable.beef, R.drawable.chicken, R.drawable.fish,
                R.drawable.pork, R.drawable.seafood, R.drawable.vegan, R.drawable.vegetarian)
            val meatFilterUnclickedIconSources = listOf(R.drawable.beef_black, R.drawable.chicken_black, R.drawable.fish_black,
                R.drawable.pork_black, R.drawable.seafood_black, R.drawable.vegan_black, R.drawable.vegetarian_black)
            val meatFilterTexts = listOf<TextView>(binding.meatFilterText1, binding.meatFilterText2, binding.meatFilterText3, binding.meatFilterText4,
                binding.meatFilterText5, binding.meatFilterText6, binding.meatFilterText7)

            val drinkFilterIcons = listOf<ImageView>(binding.drinkFilterIcon1, binding.drinkFilterIcon2, binding.drinkFilterIcon3, binding.drinkFilterIcon4,
                binding.drinkFilterIcon5, binding.drinkFilterIcon6, binding.drinkFilterIcon7)
            val drinkFilterUnclickedIconSources = listOf(R.drawable.alcohol_black, R.drawable.beer_black, R.drawable.cocktail_black,
                R.drawable.hot_black, R.drawable.mocktail_black, R.drawable.soft_black, R.drawable.wine_black)

            for(i in 0..6){
                // Unclick drink filters
                drinkFilterIcons[i].setImageResource(drinkFilterUnclickedIconSources[i])
            }
            selectedDrinkFilter = ""

            for(i in 0..6){
                meatFilterIcons[i].tag = meatFilterUnclickedIconSources[i]
                meatFilters[i].setOnClickListener {
                    val allMenuItems = getMyMenuItems()
                    val oldList = menuItems.toMutableList()
                    var filteredList: MutableList<MenuItem>
                    val preFilteredList: MutableList<MenuItem>

                    if (meatFilterIcons[i].tag == meatFilterUnclickedIconSources[i]) {
                        meatFilterIcons[i].setImageResource(meatFilterIconSources[i])
                        selectedMeatFilters.add(meatFilterTexts[i].text.toString())
                        meatFilterIcons[i].tag = meatFilterIconSources[i]

                        if(selectedCategoryFilter != "" && selectedSideFilters.isNotEmpty()){
                            val prePreFilteredList = allMenuItems.filter { it.category == selectedCategoryFilter} as MutableList
                            preFilteredList = prePreFilteredList.filter { (selectedSideFilters.intersect(it.side)).isNotEmpty() } as MutableList
                        }
                        else if(selectedCategoryFilter != ""){
                            preFilteredList = allMenuItems.filter { it.category == selectedCategoryFilter} as MutableList
                        }
                        else if(selectedSideFilters.isNotEmpty()){
                            preFilteredList = allMenuItems.filter { (selectedSideFilters.intersect(it.side)).isNotEmpty()} as MutableList
                        }
                        else{
                            preFilteredList = allMenuItems.toMutableList()
                        }
                        filteredList = preFilteredList.filter { (selectedMeatFilters.intersect(it.meat)).isNotEmpty()} as MutableList
                    }
                    else {
                        // Change background color when filter unclicked
                        meatFilterIcons[i].setImageResource(meatFilterUnclickedIconSources[i])
                        meatFilterIcons[i].tag = meatFilterUnclickedIconSources[i]
                        selectedMeatFilters.remove(meatFilterTexts[i].text.toString())

                        if(selectedCategoryFilter != "" && selectedSideFilters.isNotEmpty() && selectedMeatFilters.isNotEmpty()){
                            val prePreFilteredList = allMenuItems.filter { it.category == selectedCategoryFilter} as MutableList
                            preFilteredList = prePreFilteredList.filter { (selectedSideFilters.intersect(it.side)).isNotEmpty() } as MutableList
                            filteredList = preFilteredList.filter { (selectedMeatFilters.intersect(it.meat)).isNotEmpty() } as MutableList
                        }
                        else if(selectedCategoryFilter != "" && selectedSideFilters.isNotEmpty()){
                            val preFilteredList = allMenuItems.filter { it.category == selectedCategoryFilter} as MutableList
                            filteredList = preFilteredList.filter { (selectedSideFilters.intersect(it.side)).isNotEmpty() } as MutableList
                        }
                        else if(selectedCategoryFilter != "" && selectedMeatFilters.isNotEmpty()){
                            val preFilteredList = allMenuItems.filter { it.category == selectedCategoryFilter} as MutableList
                            filteredList = preFilteredList.filter { (selectedMeatFilters.intersect(it.meat)).isNotEmpty() } as MutableList
                        }
                        else if(selectedCategoryFilter != ""){
                            filteredList = allMenuItems.filter { it.category == selectedCategoryFilter} as MutableList
                        }
                        else if(selectedSideFilters.isNotEmpty()){
                            filteredList = allMenuItems.filter { (selectedSideFilters.intersect(it.side)).isNotEmpty() } as MutableList
                        }
                        else if(selectedMeatFilters.isNotEmpty()){
                            filteredList = allMenuItems.filter { (selectedMeatFilters.intersect(it.meat)).isNotEmpty() } as MutableList
                        }
                        else {
                            filteredList = allMenuItems
                        }
                    }
                    if(selectedAllergenFilter.isNotEmpty()){
                        filteredList = filteredList.filter { (selectedAllergenFilter.intersect(it.allergens)).isEmpty() } as MutableList
                    }
                    // Update menuItems with applied filters and reload adapter
                    menuItems.clear()
                    menuItems.addAll(filteredList)
                    mAdapter.notifyChanges(oldList, menuItems)
                }
            }
        }

        fun setSideFilters(){
            val sideFilters = listOf<CardView>(binding.cardView31, binding.cardView32, binding.cardView33, binding.cardView34, binding.cardView35, binding.cardView36, binding.cardView37)
            val sideFilterIcons = listOf<ImageView>(binding.sideFilterIcon1, binding.sideFilterIcon2, binding.sideFilterIcon3, binding.sideFilterIcon4,
                binding.sideFilterIcon5, binding.sideFilterIcon6, binding.sideFilterIcon7)
            val sideFilterIconSources = listOf(R.drawable.bread, R.drawable.dumplings, R.drawable.mushrooms,
                R.drawable.pasta, R.drawable.potatoes, R.drawable.rice, R.drawable.sauce)
            val sideFilterUnclickedIconSources = listOf(R.drawable.bread_black, R.drawable.dumplings_black, R.drawable.mushrooms_black,
                R.drawable.pasta_black, R.drawable.potatoes_black, R.drawable.rice_black, R.drawable.sauce_black)
            val sideFilterTexts = listOf<TextView>(binding.sideFilterText1, binding.sideFilterText2, binding.sideFilterText3, binding.sideFilterText4,
                binding.sideFilterText5, binding.sideFilterText6, binding.sideFilterText7)

            for(i in 0..6){
                sideFilterIcons[i].tag = sideFilterUnclickedIconSources[i]
                sideFilters[i].setOnClickListener {
                    val allMenuItems = getMyMenuItems()
                    val oldList = menuItems.toMutableList()
                    var filteredList: MutableList<MenuItem>
                    val preFilteredList: MutableList<MenuItem>

                    val drinkFilterIcons = listOf<ImageView>(binding.drinkFilterIcon1, binding.drinkFilterIcon2, binding.drinkFilterIcon3, binding.drinkFilterIcon4,
                        binding.drinkFilterIcon5, binding.drinkFilterIcon6, binding.drinkFilterIcon7)
                    val drinkFilterUnclickedIconSources = listOf(R.drawable.alcohol_black, R.drawable.beer_black, R.drawable.cocktail_black,
                        R.drawable.hot_black, R.drawable.mocktail_black, R.drawable.soft_black, R.drawable.wine_black)

                    for(i in 0..6){
                        // Unclick drink filters
                        drinkFilterIcons[i].setImageResource(drinkFilterUnclickedIconSources[i])
                    }
                    selectedDrinkFilter = ""

                    if (sideFilterIcons[i].tag == sideFilterUnclickedIconSources[i]) {
                        sideFilterIcons[i].setImageResource(sideFilterIconSources[i])
                        selectedSideFilters.add(sideFilterTexts[i].text.toString())
                        sideFilterIcons[i].tag = sideFilterIconSources[i]

                        if(selectedCategoryFilter != "" && selectedMeatFilters.isNotEmpty()){
                            val prePreFilteredList = allMenuItems.filter { it.category == selectedCategoryFilter} as MutableList
                            preFilteredList = prePreFilteredList.filter { (selectedMeatFilters.intersect(it.meat)).isNotEmpty() } as MutableList
                        }
                        else if(selectedCategoryFilter != ""){
                            preFilteredList = allMenuItems.filter { it.category == selectedCategoryFilter} as MutableList
                        }
                        else if(selectedMeatFilters.isNotEmpty()){
                            preFilteredList = allMenuItems.filter { (selectedMeatFilters.intersect(it.meat)).isNotEmpty()} as MutableList
                        }
                        else{
                            preFilteredList = allMenuItems.toMutableList()
                        }
                        filteredList = preFilteredList.filter { (selectedSideFilters.intersect(it.side)).isNotEmpty()} as MutableList
                    }
                    else {
                        // Change background color when filter unclicked
                        sideFilterIcons[i].setImageResource(sideFilterUnclickedIconSources[i])
                        sideFilterIcons[i].tag = sideFilterUnclickedIconSources[i]
                        selectedSideFilters.remove(sideFilterTexts[i].text.toString())

                        if(selectedCategoryFilter != "" && selectedMeatFilters.isNotEmpty() && selectedSideFilters.isNotEmpty()){
                            val prePreFilteredList = allMenuItems.filter { it.category == selectedCategoryFilter} as MutableList
                            val preFilteredList = prePreFilteredList.filter { (selectedMeatFilters.intersect(it.meat)).isNotEmpty() } as MutableList
                            filteredList = preFilteredList.filter { (selectedSideFilters.intersect(it.side)).isNotEmpty() } as MutableList
                        }
                        else if(selectedCategoryFilter != "" && selectedMeatFilters.isNotEmpty()){
                            val preFilteredList = allMenuItems.filter { it.category == selectedCategoryFilter} as MutableList
                            filteredList = preFilteredList.filter { (selectedMeatFilters.intersect(it.meat)).isNotEmpty() } as MutableList
                        }
                        else if(selectedCategoryFilter != "" && selectedSideFilters.isNotEmpty()){
                            val preFilteredList = allMenuItems.filter { it.category == selectedCategoryFilter} as MutableList
                            filteredList = preFilteredList.filter { (selectedSideFilters.intersect(it.side)).isNotEmpty() } as MutableList
                        }
                        else if(selectedCategoryFilter != ""){
                            filteredList = allMenuItems.filter { it.category == selectedCategoryFilter} as MutableList
                        }
                        else if(selectedMeatFilters.isNotEmpty()){
                            filteredList = allMenuItems.filter { (selectedMeatFilters.intersect(it.meat)).isNotEmpty() } as MutableList
                        }
                        else if(selectedSideFilters.isNotEmpty()){
                            filteredList = allMenuItems.filter { (selectedSideFilters.intersect(it.side)).isNotEmpty() } as MutableList
                        }
                        else {
                            filteredList = allMenuItems
                        }
                    }
                    if(selectedAllergenFilter.isNotEmpty()){
                        filteredList = filteredList.filter { (selectedAllergenFilter.intersect(it.allergens)).isEmpty() } as MutableList
                    }
                    // Update menuItems with applied filters and reload adapter
                    menuItems.clear()
                    menuItems.addAll(filteredList)
                    mAdapter.notifyChanges(oldList, menuItems)
                }
            }
        }

        fun setDrinkFilters(){
            val drinkFilters = listOf<CardView>(binding.cardView41, binding.cardView42, binding.cardView43, binding.cardView44, binding.cardView45, binding.cardView46, binding.cardView47)
            val drinkFilterIcons = listOf<ImageView>(binding.drinkFilterIcon1, binding.drinkFilterIcon2, binding.drinkFilterIcon3, binding.drinkFilterIcon4,
                binding.drinkFilterIcon5, binding.drinkFilterIcon6, binding.drinkFilterIcon7)
            val drinkFilterIconSources = listOf(R.drawable.alcohol, R.drawable.beer, R.drawable.cocktail,
                R.drawable.hot, R.drawable.mocktail, R.drawable.soft, R.drawable.wine)
            val drinkFilterUnclickedIconSources = listOf(R.drawable.alcohol_black, R.drawable.beer_black, R.drawable.cocktail_black,
                R.drawable.hot_black, R.drawable.mocktail_black, R.drawable.soft_black, R.drawable.wine_black)
            val drinkFilterTexts = listOf<TextView>(binding.drinkFilterText1, binding.drinkFilterText2, binding.drinkFilterText3, binding.drinkFilterText4,
                binding.drinkFilterText5, binding.drinkFilterText6, binding.drinkFilterText7)

            val meatFilterIcons = listOf<ImageView>(binding.meatFilterIcon1, binding.meatFilterIcon2, binding.meatFilterIcon3, binding.meatFilterIcon4,
                binding.meatFilterIcon5, binding.meatFilterIcon6, binding.meatFilterIcon7)
            val meatFilterUnclickedIconSources = listOf(R.drawable.beef_black, R.drawable.chicken_black, R.drawable.fish_black,
                R.drawable.pork_black, R.drawable.seafood_black, R.drawable.vegan_black, R.drawable.vegetarian_black)
            val sideFilterIcons = listOf<ImageView>(binding.sideFilterIcon1, binding.sideFilterIcon2, binding.sideFilterIcon3, binding.sideFilterIcon4,
                binding.sideFilterIcon5, binding.sideFilterIcon6, binding.sideFilterIcon7)
            val sideFilterUnclickedIconSources = listOf(R.drawable.bread_black, R.drawable.dumplings_black, R.drawable.mushrooms_black,
                R.drawable.pasta_black, R.drawable.potatoes_black, R.drawable.rice_black, R.drawable.sauce_black)

            for(i in 0..6){
                drinkFilterIcons[i].tag = drinkFilterUnclickedIconSources[i]
                drinkFilters[i].setOnClickListener {
                    val allMenuItems = getMyMenuItems()
                    val oldList = menuItems.toMutableList()
                    var filteredList = mutableListOf<MenuItem>()

                    if (drinkFilterIcons[i].tag == drinkFilterUnclickedIconSources[i]) {
                        for(i in 0..6){
                            // Unclick meat and side filters
                            meatFilterIcons[i].setImageResource(meatFilterUnclickedIconSources[i])
                            sideFilterIcons[i].setImageResource(sideFilterUnclickedIconSources[i])
                        }
                        selectedMeatFilters.clear()
                        selectedSideFilters.clear()
                        for(i in 0..6){
                            // Unclick drink filters
                            drinkFilterIcons[i].setImageResource(drinkFilterUnclickedIconSources[i])
                        }
                        // Change icon color when filter clicked
                        drinkFilterIcons[i].setImageResource(drinkFilterIconSources[i])
                        drinkFilterIcons[i].tag = drinkFilterIconSources[i]
                        selectedDrinkFilter = drinkFilterTexts[i].text.toString()
                        if (selectedCategoryFilter !=""){
                            val preFilteredList = allMenuItems.filter { it.category == selectedCategoryFilter }
                            filteredList = preFilteredList.filter { it.drink == selectedDrinkFilter } as MutableList
                        }
                        else{
                            filteredList = allMenuItems.filter { it.drink == selectedDrinkFilter } as MutableList
                        }
                    }
                    else {
                        // Change icon color when filter unclicked
                        drinkFilterIcons[i].setImageResource(drinkFilterUnclickedIconSources[i])
                        drinkFilterIcons[i].tag = drinkFilterUnclickedIconSources[i]
                        selectedDrinkFilter = ""
                        if (selectedCategoryFilter !=""){
                            filteredList = allMenuItems.filter { it.category == selectedCategoryFilter } as MutableList
                        }
                        else{
                            filteredList = allMenuItems.toMutableList()
                        }
                    }
                    if(selectedAllergenFilter.isNotEmpty()){
                        filteredList = filteredList.filter { (selectedAllergenFilter.intersect(it.allergens)).isEmpty() } as MutableList
                    }
                    // Update menuItems with applied filter and reload adapter
                    menuItems.clear()
                    menuItems.addAll(filteredList)
                    mAdapter.notifyChanges(oldList, menuItems)
                }
            }
        }

        fun setAllergenFilters(){
            val allergenFilters = listOf<CardView>(binding.cardView52, binding.cardView53, binding.cardView54, binding.cardView55, binding.cardView56, binding.cardView57,
                binding.cardView58, binding.cardView59, binding.cardView60, binding.cardView61, binding.cardView62, binding.cardView63, binding.cardView64, binding.cardView65)
            val allergenFilterTexts = listOf<TextView>(binding.allergyFilterText2, binding.allergyFilterText3, binding.allergyFilterText4, binding.allergyFilterText5,
                binding.allergyFilterText6, binding.allergyFilterText7, binding.allergyFilterText8, binding.allergyFilterText9, binding.allergyFilterText10,
                binding.allergyFilterText11, binding.allergyFilterText12, binding.allergyFilterText13, binding.allergyFilterText14, binding.allergyFilterText15)

            for(i in 0..13){
                allergenFilters[i].setOnClickListener {
                    val allMenuItems = getMyMenuItems()
                    val oldList = menuItems.toMutableList()
                    var filteredList = mutableListOf<MenuItem>()
                    val preFilteredList: MutableList<MenuItem>

                    if (allergenFilters[i].cardBackgroundColor.getColorForState(
                            allergenFilters[i].drawableState, Color.parseColor("#DADADA")) != Color.GRAY) {
                        allergenFilters[i].setCardBackgroundColor(Color.GRAY)
                        selectedAllergenFilter.add(allergenFilterTexts[i].text.toString())

                        if(selectedMeatFilters.isNotEmpty() && selectedSideFilters.isNotEmpty()) {
                            val prePreFilteredList = allMenuItems.filter { (selectedMeatFilters.intersect(it.meat)).isNotEmpty() } as MutableList<MenuItem>
                            preFilteredList = prePreFilteredList.filter { (selectedSideFilters.intersect(it.side)).isNotEmpty() } as MutableList<MenuItem>
                        }
                        else if(selectedMeatFilters.isNotEmpty()){
                            preFilteredList = allMenuItems.filter { (selectedMeatFilters.intersect(it.meat)).isNotEmpty() } as MutableList<MenuItem>
                        }
                        else if(selectedSideFilters.isNotEmpty()){
                            preFilteredList = allMenuItems.filter { (selectedSideFilters.intersect(it.side)).isNotEmpty() } as MutableList<MenuItem>
                        }
                        else if(selectedDrinkFilter != ""){
                            preFilteredList = allMenuItems.filter { it.drink == selectedDrinkFilter } as MutableList
                        }
                        else{
                            preFilteredList = allMenuItems.toMutableList()
                        }
                        if(selectedCategoryFilter != ""){
                            val categoryFilteredList = preFilteredList.filter { it.category == selectedCategoryFilter }
                            filteredList = categoryFilteredList.filter { (selectedAllergenFilter.intersect(it.allergens)).isEmpty() } as MutableList
                        }
                        else{
                            filteredList = preFilteredList.filter { (selectedAllergenFilter.intersect(it.allergens)).isEmpty() } as MutableList
                        }
                    }
                    else {
                        // Change background color when filter unclicked
                        allergenFilters[i].setCardBackgroundColor(Color.parseColor("#DADADA"))
                        selectedAllergenFilter.remove(allergenFilterTexts[i].text.toString())
                        if(selectedMeatFilters.isNotEmpty() && selectedSideFilters.isNotEmpty()){
                            val preFilteredList = allMenuItems.filter { (selectedMeatFilters.intersect(it.meat)).isNotEmpty() } as MutableList<MenuItem>
                            filteredList = preFilteredList.filter { (selectedSideFilters.intersect(it.side)).isNotEmpty() } as MutableList<MenuItem>
                        }
                        else if(selectedMeatFilters.isNotEmpty()) {
                            filteredList = allMenuItems.filter { (selectedMeatFilters.intersect(it.meat)).isNotEmpty() } as MutableList<MenuItem>
                        }
                        else if(selectedSideFilters.isNotEmpty()){
                            filteredList = allMenuItems.filter { (selectedSideFilters.intersect(it.side)).isNotEmpty() } as MutableList<MenuItem>
                        }
                        else if(selectedDrinkFilter != ""){
                            filteredList = allMenuItems.filter { it.drink == selectedDrinkFilter } as MutableList
                        }
                        else{
                            filteredList = allMenuItems
                        }
                        if(selectedCategoryFilter != ""){
                            filteredList = filteredList.filter { it.category == selectedCategoryFilter } as MutableList
                        }
                        if(selectedAllergenFilter.isNotEmpty()){
                            filteredList = filteredList.filter { (selectedAllergenFilter.intersect(it.allergens)).isEmpty() } as MutableList
                        }
                    }
                    // Update menuItems with applied filter and reload adapter
                    menuItems.clear()
                    menuItems.addAll(filteredList)
                    mAdapter.notifyChanges(oldList, menuItems)
                }
            }
        }

        setCategoryFilters()
        setMeatFilters()
        setSideFilters()
        setDrinkFilters()
        setAllergenFilters()

        // Hide or show filters
        binding.filterButton.setOnClickListener {
            val scrollViews = listOf<HorizontalScrollView>(binding.horizontalScrollView1, binding.horizontalScrollView2,
                binding.horizontalScrollView3, binding.horizontalScrollView4, binding.horizontalScrollView5)
            val views = listOf<View>(binding.view1, binding.view2, binding.view3, binding.view4)
            if (scrollViews[0].isVisible){
                scrollViews[4].visibility = View.GONE
                for (i in 3 downTo 0){
                    views[i].visibility = View.GONE
                    scrollViews[i].visibility = View.GONE
                }
            }
            else{
                for (i in 0..3){
                    scrollViews[i].isVisible = true
                    views[i].isVisible = true
                }
                scrollViews[4].isVisible = true
            }
        }

        // SearchView to filer menu items
        binding.searchView2.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Submit function undefined
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val allMenuItems = getMyMenuItems()
                val oldList = menuItems.toMutableList()
                val filteredList = mutableListOf<MenuItem>()
                // Search in menu items for names containing searched string
                if(newText!!.isNotEmpty()){
                    val search = newText.toLowerCase()
                    allMenuItems.forEach {
                        if(it.name.toLowerCase().contains(search)){
                            filteredList.add(it)
                        }
                    }
                }
                // If search string is empty list all items
                else{
                    filteredList.addAll(allMenuItems)
                }
                // If no matching items were found show "No results"
                if(filteredList.isEmpty()){
                    binding.noResultsText.visibility = View.VISIBLE
                }
                else{
                    binding.noResultsText.visibility = View.GONE
                }
                // Update adapter
                menuItems.clear()
                menuItems.addAll(filteredList)
                mAdapter.notifyChanges(oldList, menuItems)
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
