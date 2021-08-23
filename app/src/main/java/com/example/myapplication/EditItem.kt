package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.myapplication.data.MenuItem
import com.example.myapplication.data.MenuItemData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_edit_item.*
import kotlinx.android.synthetic.main.fragment_edit_item.Beef
import kotlinx.android.synthetic.main.fragment_edit_item.Bread
import kotlinx.android.synthetic.main.fragment_edit_item.Celery
import kotlinx.android.synthetic.main.fragment_edit_item.Chicken
import kotlinx.android.synthetic.main.fragment_edit_item.Crustaceans
import kotlinx.android.synthetic.main.fragment_edit_item.Dumplings
import kotlinx.android.synthetic.main.fragment_edit_item.Eggs
import kotlinx.android.synthetic.main.fragment_edit_item.Fish
import kotlinx.android.synthetic.main.fragment_edit_item.Fish_allergen
import kotlinx.android.synthetic.main.fragment_edit_item.Gluten
import kotlinx.android.synthetic.main.fragment_edit_item.Lupin
import kotlinx.android.synthetic.main.fragment_edit_item.Milk
import kotlinx.android.synthetic.main.fragment_edit_item.Molluscs
import kotlinx.android.synthetic.main.fragment_edit_item.Mushrooms
import kotlinx.android.synthetic.main.fragment_edit_item.Mustard
import kotlinx.android.synthetic.main.fragment_edit_item.Nuts
import kotlinx.android.synthetic.main.fragment_edit_item.Pasta
import kotlinx.android.synthetic.main.fragment_edit_item.Peanuts
import kotlinx.android.synthetic.main.fragment_edit_item.Pork
import kotlinx.android.synthetic.main.fragment_edit_item.Potatoes
import kotlinx.android.synthetic.main.fragment_edit_item.Rice
import kotlinx.android.synthetic.main.fragment_edit_item.Sauce
import kotlinx.android.synthetic.main.fragment_edit_item.Seafood
import kotlinx.android.synthetic.main.fragment_edit_item.Sesame
import kotlinx.android.synthetic.main.fragment_edit_item.Soya
import kotlinx.android.synthetic.main.fragment_edit_item.Sulphur_dioxide
import kotlinx.android.synthetic.main.fragment_edit_item.Vegan
import kotlinx.android.synthetic.main.fragment_edit_item.Vegetarian
import kotlinx.android.synthetic.main.fragment_edit_item.itemAddCategory
import kotlinx.android.synthetic.main.fragment_edit_item.itemAddCurrency
import kotlinx.android.synthetic.main.fragment_edit_item.itemAddDescription
import kotlinx.android.synthetic.main.fragment_edit_item.itemAddDrink
import kotlinx.android.synthetic.main.fragment_edit_item.itemAddName
import kotlinx.android.synthetic.main.fragment_edit_item.itemAddPrice

// Fragment handling editing of an existing Menu Item
class EditItem : Fragment() {

    // Using Jetpack's Navigation Component library to send data between fragments
    private val args: EditItemArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve menuItems from shared pref menu_items on reload
        // If no menuItems are saved return original MenuItemData
        fun getMyMenuItems(): MutableList<MenuItem> {
            val myMenuItems =
                requireActivity().getSharedPreferences("menu_items", Context.MODE_PRIVATE)
                    .getString("menu_items", "default value")
            if (myMenuItems != "default value") {
                val gson = Gson()
                val listType = object : TypeToken<MutableList<MenuItem>>() {}.type
                val obj = gson.fromJson<MutableList<MenuItem>>(myMenuItems, listType)
                return obj
            } else {
                return MenuItemData().allMenuItems
            }
        }

        // Get the ID of Menu Item selected for editing from navArgs
        val productID = args.menuItemIdArgument
        val currentMenuItems = getMyMenuItems()
        // Fetch the menu item data selected
        val menuItem = currentMenuItems.find { it.id == productID }
        // If Menu Item retrieved successfully..
        if (menuItem != null) {
            val categoryFilters = listOf("Starter", "Soup", "Main", "Dessert", "Drink")
            val drinkFilters = listOf("", "Alcohol", "Beer", "Cocktail", "Hot", "Mocktail", "Soft", "Wine")
            val meatFilters = listOf(Beef, Chicken, Fish, Pork, Seafood, Vegan, Vegetarian)
            val sideFilters = listOf(Bread, Dumplings, Mushrooms, Pasta, Potatoes, Rice, Sauce)
            val allergyFilters = listOf(Celery, Crustaceans, Eggs, Fish, Gluten,
                Lupin, Milk, Molluscs, Mustard, Nuts, Peanuts, Sesame, Soya, Sulphur_dioxide)

            // Fill in original MenuItem information
            itemAddName.setText(menuItem.name)
            itemAddCurrency.setText(menuItem.currency)
            itemAddPrice.setText(menuItem.price.toString())
            itemAddDescription.setText(menuItem.description)
            itemAddCategory.setSelection(categoryFilters.indexOf(menuItem.category))
            itemAddDrink.setSelection(drinkFilters.indexOf(menuItem.drink))
            val meatChecked = meatFilters.filter { it.text.toString() in menuItem.meat }
            meatChecked.forEach { it.isChecked = true }
            val sideChecked = sideFilters.filter { it.text.toString() in menuItem.side }
            sideChecked.forEach { it.isChecked = true }
            val allergyChecked = allergyFilters.filter { it.text.toString() in menuItem.allergens }
            allergyChecked.forEach { it.isChecked = true }
            Glide.with(this)
                .load(menuItem.photo.toUri())
                .into(imageView3)
        }

        // On click of Return button go back to menu list (First Fragment)
        returnToFirstFragButton.setOnClickListener {
            findNavController().navigate(R.id.action_editItem_to_FirstFragment)
        }

        // On click of Change photo button go to add photo
        addPhotoButton2.setOnClickListener {
            findNavController().navigate(R.id.action_editItem_to_permissionChecker)
        }

        // On click of Save button
        saveItemToMenuButton.setOnClickListener {
            // Check if dish name, currency or price is empty and alert if is
            if (itemAddName.text.toString().isBlank()) {
                Toast.makeText(activity, "Dish name cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (itemAddCurrency.text.toString().isBlank()) {
                Toast.makeText(activity, "Currency cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (itemAddPrice.text.toString().isBlank()) {
                Toast.makeText(activity, "Price cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (itemAddPrice.text.toString().toFloat() > 99999.99) {
                Toast.makeText(activity, "Price cannot be more than 5 digits", Toast.LENGTH_SHORT).show()
            }
            // If necessary values are filled
            else {
                // Assign newly chosen photo if any or old photo
                val photo = if (requireActivity().getSharedPreferences("photo", Context.MODE_PRIVATE).getString("photo", "default value") == "default value")
                    menuItem?.photo
                    else requireActivity().getSharedPreferences("photo", Context.MODE_PRIVATE).getString("photo", "default value")

                // Create new MenuItem
                val newItem = MenuItem(
                    productID, itemAddName.text.toString(), itemAddCurrency.text.toString(),
                    itemAddPrice.text.toString().toDouble(), itemAddDescription.text.toString(),
                    itemAddCategory.selectedItem.toString(),
                    listOf(Beef, Chicken, Fish, Pork, Seafood, Vegan, Vegetarian).filter {
                        it.isChecked
                    }.map {
                        it.text.toString()
                    },
                    listOf(Bread, Dumplings, Mushrooms, Pasta, Potatoes, Rice, Sauce).filter {
                        it.isChecked
                    }.map {
                        it.text.toString()
                    },
                    itemAddDrink.selectedItem.toString(),
                    listOf(
                        Celery, Crustaceans, Eggs, Fish_allergen, Gluten, Lupin, Milk, Molluscs,
                        Mustard, Nuts, Peanuts, Sesame, Soya, Sulphur_dioxide
                    ).filter {
                        it.isChecked
                    }.map {
                        it.text.toString()
                    },
                    photo = photo!!
                )

                val position = currentMenuItems.indexOf(menuItem)

                // Remove the old item
                currentMenuItems.remove(menuItem)
                // Paste the edited item
                currentMenuItems.add(position, newItem)

                val gson = Gson()
                val menuItems = gson.toJson(currentMenuItems)
                // Save updated MenuItems list to shared pref.
                requireActivity().getSharedPreferences("menu_items", Context.MODE_PRIVATE).edit()
                    .apply {
                        putString("menu_items", menuItems)
                    }.apply()
                // Delete the saved NewItem in shared pref. in case it was the edited one
                requireActivity().getSharedPreferences("new_item", Context.MODE_PRIVATE).edit()
                    .apply {
                        putString("new_item", "default value")
                    }.apply()
                // Delete photo in shared preferences
                requireActivity().getSharedPreferences("photo", Context.MODE_PRIVATE).edit().remove("photo").apply()

                // Navigate back to main screen
                findNavController().navigate(R.id.action_editItem_to_FirstFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Get saved photo from shared pref photo and load it to image view
        val photo = requireActivity().getSharedPreferences("photo", Context.MODE_PRIVATE).getString("photo", "default value")
        if(photo != "default value"){
            Glide.with(this)
                .load(photo?.toUri())
                .into(imageView3)
        }
    }
}