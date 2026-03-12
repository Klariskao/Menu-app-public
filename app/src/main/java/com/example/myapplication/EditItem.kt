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
import com.example.myapplication.databinding.FragmentEditItemBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Fragment handling editing of an existing Menu Item
class EditItem : Fragment() {

    // Using Jetpack's Navigation Component library to send data between fragments
    private val args: EditItemArgs by navArgs()
    private var _binding: FragmentEditItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditItemBinding.inflate(inflater, container, false)
        return binding.root
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
            val meatFilters = listOf(binding.Beef, binding.Chicken, binding.Fish, binding.Pork, binding.Seafood, binding.Vegan, binding.Vegetarian)
            val sideFilters = listOf(binding.Bread, binding.Dumplings, binding.Mushrooms, binding.Pasta, binding.Potatoes, binding.Rice, binding.Sauce)
            val allergyFilters = listOf(binding.Celery, binding.Crustaceans, binding.Eggs, binding.FishAllergen, binding.Gluten,
                binding.Lupin, binding.Milk, binding.Molluscs, binding.Mustard, binding.Nuts, binding.Peanuts, binding.Sesame, binding.Soya, binding.SulphurDioxide)

            // Fill in original MenuItem information
            binding.itemAddName.setText(menuItem.name)
            binding.itemAddCurrency.setText(menuItem.currency)
            binding.itemAddPrice.setText(menuItem.price.toString())
            binding.itemAddDescription.setText(menuItem.description)
            binding.itemAddCategory.setSelection(categoryFilters.indexOf(menuItem.category))
            binding.itemAddDrink.setSelection(drinkFilters.indexOf(menuItem.drink))
            val meatChecked = meatFilters.filter { it.text.toString() in menuItem.meat }
            meatChecked.forEach { it.isChecked = true }
            val sideChecked = sideFilters.filter { it.text.toString() in menuItem.side }
            sideChecked.forEach { it.isChecked = true }
            val allergyChecked = allergyFilters.filter { it.text.toString() in menuItem.allergens }
            allergyChecked.forEach { it.isChecked = true }
            Glide.with(this)
                .load(menuItem.photo.toUri())
                .into(binding.imageView3)
        }

        // On click of Return button go back to menu list (First Fragment)
        binding.returnToFirstFragButton.setOnClickListener {
            findNavController().navigate(R.id.action_editItem_to_FirstFragment)
        }

        // On click of Change photo button go to add photo
        binding.addPhotoButton2.setOnClickListener {
            findNavController().navigate(R.id.action_editItem_to_permissionChecker)
        }

        // On click of Save button
        binding.saveItemToMenuButton.setOnClickListener {
            // Check if dish name, currency or price is empty and alert if is
            if (binding.itemAddName.text.toString().isBlank()) {
                Toast.makeText(activity, "Dish name cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (binding.itemAddCurrency.text.toString().isBlank()) {
                Toast.makeText(activity, "Currency cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (binding.itemAddPrice.text.toString().isBlank()) {
                Toast.makeText(activity, "Price cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (binding.itemAddPrice.text.toString().toFloat() > 99999.99) {
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
                    productID, binding.itemAddName.text.toString(), binding.itemAddCurrency.text.toString(),
                    binding.itemAddPrice.text.toString().toDouble(), binding.itemAddDescription.text.toString(),
                    binding.itemAddCategory.selectedItem.toString(),
                    listOf(binding.Beef, binding.Chicken, binding.Fish, binding.Pork, binding.Seafood, binding.Vegan, binding.Vegetarian).filter {
                        it.isChecked
                    }.map {
                        it.text.toString()
                    },
                    listOf(binding.Bread, binding.Dumplings, binding.Mushrooms, binding.Pasta, binding.Potatoes, binding.Rice, binding.Sauce).filter {
                        it.isChecked
                    }.map {
                        it.text.toString()
                    },
                    binding.itemAddDrink.selectedItem.toString(),
                    listOf(
                        binding.Celery, binding.Crustaceans, binding.Eggs, binding.FishAllergen, binding.Gluten, binding.Lupin, binding.Milk, binding.Molluscs,
                        binding.Mustard, binding.Nuts, binding.Peanuts, binding.Sesame, binding.Soya, binding.SulphurDioxide
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
                .into(binding.imageView3)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
