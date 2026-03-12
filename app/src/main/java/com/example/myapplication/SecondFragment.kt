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
import com.bumptech.glide.Glide
import com.example.myapplication.data.MenuItem
import com.example.myapplication.data.MenuItemData
import com.example.myapplication.databinding.FragmentSecondBinding
import com.google.gson.Gson

// Fragment to add a new Menu Item
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // On click of Change photo button go to add photo screen
        binding.addPhotoButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_permissionChecker)
        }

        // On click of Return button go back to menu list
        binding.returnButton.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        // On click of Add button
        binding.addItemToMenuButton.setOnClickListener {
            // Check if dish name, currency or price is empty and alert if is
            if (binding.itemAddName.text.toString().isBlank()) {
                Toast.makeText(activity, "Dish name cannot be empty", Toast.LENGTH_SHORT).show()
            }
            else if (binding.itemAddCurrency.text.toString().isBlank()) {
                Toast.makeText(activity, "Currency cannot be empty", Toast.LENGTH_SHORT).show()
            }
            else if (binding.itemAddPrice.text.toString().isBlank()) {
                Toast.makeText(activity, "Price cannot be empty", Toast.LENGTH_SHORT).show()
            }
            else if (binding.itemAddPrice.text.toString().toFloat() > 99999.99) {
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
                    MenuItem(id.toString(), binding.itemAddName.text.toString(), binding.itemAddCurrency.text.toString(),
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
                        listOf(binding.Celery, binding.Crustaceans, binding.Eggs, binding.FishAllergen, binding.Gluten, binding.Lupin, binding.Milk, binding.Molluscs,
                            binding.Mustard, binding.Nuts, binding.Peanuts, binding.Sesame, binding.Soya, binding.SulphurDioxide).filter {
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
                .into(binding.imageView2)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
