package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myapplication.data.MenuItem
import com.example.myapplication.data.MenuItemData
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_edit_item.*
import kotlinx.android.synthetic.main.fragment_second.*
import kotlinx.android.synthetic.main.fragment_second.Beef
import kotlinx.android.synthetic.main.fragment_second.Bread
import kotlinx.android.synthetic.main.fragment_second.Celery
import kotlinx.android.synthetic.main.fragment_second.Chicken
import kotlinx.android.synthetic.main.fragment_second.Crustaceans
import kotlinx.android.synthetic.main.fragment_second.Dumplings
import kotlinx.android.synthetic.main.fragment_second.Eggs
import kotlinx.android.synthetic.main.fragment_second.Fish
import kotlinx.android.synthetic.main.fragment_second.Fish_allergen
import kotlinx.android.synthetic.main.fragment_second.Gluten
import kotlinx.android.synthetic.main.fragment_second.Lupin
import kotlinx.android.synthetic.main.fragment_second.Milk
import kotlinx.android.synthetic.main.fragment_second.Molluscs
import kotlinx.android.synthetic.main.fragment_second.Mushrooms
import kotlinx.android.synthetic.main.fragment_second.Mustard
import kotlinx.android.synthetic.main.fragment_second.Nuts
import kotlinx.android.synthetic.main.fragment_second.Pasta
import kotlinx.android.synthetic.main.fragment_second.Peanuts
import kotlinx.android.synthetic.main.fragment_second.Pork
import kotlinx.android.synthetic.main.fragment_second.Potatoes
import kotlinx.android.synthetic.main.fragment_second.Rice
import kotlinx.android.synthetic.main.fragment_second.Sauce
import kotlinx.android.synthetic.main.fragment_second.Seafood
import kotlinx.android.synthetic.main.fragment_second.Sesame
import kotlinx.android.synthetic.main.fragment_second.Soya
import kotlinx.android.synthetic.main.fragment_second.Sulphur_dioxide
import kotlinx.android.synthetic.main.fragment_second.Vegan
import kotlinx.android.synthetic.main.fragment_second.Vegetarian
import kotlinx.android.synthetic.main.fragment_second.itemAddCategory
import kotlinx.android.synthetic.main.fragment_second.itemAddCurrency
import kotlinx.android.synthetic.main.fragment_second.itemAddDescription
import kotlinx.android.synthetic.main.fragment_second.itemAddDrink
import kotlinx.android.synthetic.main.fragment_second.itemAddName
import kotlinx.android.synthetic.main.fragment_second.itemAddPrice

// Fragment to add a new Menu Item
class SecondFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // On click of Change photo button go to add photo screen
        addPhotoButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_permissionChecker)
        }

        // On click of Return button go back to menu list
        returnButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        // On click of Add button
        addItemToMenuButton.setOnClickListener {
            // Check if dish name, currency or price is empty and alert if is
            if (itemAddName.text.toString().isBlank()) {
                Toast.makeText(activity, "Dish name cannot be empty", Toast.LENGTH_SHORT).show()
            }
            else if (itemAddCurrency.text.toString().isBlank()) {
                Toast.makeText(activity, "Currency cannot be empty", Toast.LENGTH_SHORT).show()
            }
            else if (itemAddPrice.text.toString().isBlank()) {
                Toast.makeText(activity, "Price cannot be empty", Toast.LENGTH_SHORT).show()
            }
            else if (itemAddPrice.text.toString().toFloat() > 99999.99) {
                Toast.makeText(activity, "Price cannot be more than 5 digits", Toast.LENGTH_SHORT).show()
            }
            // If necessary values are filled
            else{
                // Assign the ID + 1 of last assigned item or original MenuItemData size + 1
                var id = requireActivity().getSharedPreferences("dish_id", Context.MODE_PRIVATE).getString("dish_id", (MenuItemData().allMenuItems.size + 1).toString())
                // Assign chosen photo if any or default salad photo if none
                val photo = if (requireActivity().getSharedPreferences("photo", Context.MODE_PRIVATE).getString("photo", "default value") == "default value")
                    "android.resource://com.example.myapplication/" + R.drawable.food
                    else requireActivity().getSharedPreferences("photo", Context.MODE_PRIVATE).getString("photo", "default value")

                // Create new MenuItem
                val newItem =
                    MenuItem(id.toString(), itemAddName.text.toString(), itemAddCurrency.text.toString(),
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
                        listOf(Celery, Crustaceans, Eggs, Fish_allergen, Gluten, Lupin, Milk, Molluscs,
                            Mustard, Nuts, Peanuts, Sesame, Soya, Sulphur_dioxide).filter {
                            it.isChecked
                        }.map {
                            it.text.toString()
                        },
                        photo = photo!!
                    )

                // Save new ID to shared preferences
                id = (id?.toInt()?.plus(1)).toString()
                requireActivity().getSharedPreferences("dish_id", Context.MODE_PRIVATE).edit().apply {
                    putString("dish_id", id)
                }.apply()

                // Save new MenuItem to shared preferences
                saveNewItem(newItem)

                // Delete photo in shared preferences
                requireActivity().getSharedPreferences("photo", Context.MODE_PRIVATE).edit().remove("photo").apply()

                // Return to First Fragment
                findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val photo = requireActivity().getSharedPreferences("photo", Context.MODE_PRIVATE).getString("photo", "default value")
        if(photo != "default value"){
            Glide.with(this)
                .load(photo?.toUri())
                .into(imageView2)
        }
    }

    // Save new MenuItem to shared preferences file new_item as JSON new_item
    private fun saveNewItem(newItem: MenuItem) {
        val gson = Gson()
        val newItemText = gson.toJson(newItem)
        requireActivity().getSharedPreferences("new_item", Context.MODE_PRIVATE).edit().apply {
            putString("new_item", newItemText)
        }.apply()
    }
}

