package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.data.MenuItem
import com.example.myapplication.data.MenuItemData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.menu_item_info.*

// Fragment showing information on a selected Menu Item
class MenuItemInfo : Fragment() {

    // Using Jetpack's Navigation Component library to send data between fragments
    private val args: MenuItemInfoArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.menu_item_info, container, false)
    }

    // Render menu item info on the page
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Render info accessed through the args passed
        val productID = args.menuItemIDArgument

        // Retrieve menuItems from shared pref menu_items on reload
        // If no menuItems are saved return original MenuItemData
        fun getMyMenuItems(): MutableList<MenuItem> {
            val myMenuItems = requireActivity().getSharedPreferences("menu_items", Context.MODE_PRIVATE).getString("menu_items", "default value")
            if (myMenuItems != "default value") {
                val gson = Gson()
                val listType = object : TypeToken<MutableList<MenuItem>>() { }.type
                val obj = gson.fromJson<MutableList<MenuItem>>(myMenuItems, listType)
                return obj
            }
            else {
                return MenuItemData().allMenuItems
            }
        }

        // Fetch the menu item data selected
        val menuItem = getMyMenuItems().find {it.id == productID }

        // If data retrieved successfully, render the info
        if(menuItem != null) {
            menuItemNameView.text = menuItem.name
            menuItemDescriptionView.text = menuItem.description
            menuItemPriceView.text = "${menuItem.currency}${"%.2f".format(menuItem.price)}"
            Glide.with(this)
                .load(menuItem.photo.toUri())
                .into(menuItemPhotoView)
            ratingBarSetter.rating = (menuItem.rating_total / menuItem.rated_by).toFloat()

            menuItemAllergenesView.apply {
                val myLayoutManager = LinearLayoutManager(activity)
                myLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                layoutManager = myLayoutManager
                adapter = AllergensAdapter(menuItem.allergens)
            }

            // On click on SUBMIT RATING save rating
            submitRatingButton.setOnClickListener {
                val currentMenuItems = getMyMenuItems()
                val position = currentMenuItems.indexOf(menuItem)

                menuItem.rated_by += 1
                menuItem.rating_total += ratingBarSetter.rating.toDouble()
                Toast.makeText(activity, "Thank you for your rating", Toast.LENGTH_SHORT).show()
                // Disable the button so that no more ratings can be submitted
                submitRatingButton.isClickable = false

                // Paste the edited item
                currentMenuItems[position] = menuItem

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
            }

            // On click on DELETE remove item from MenuItems and return to main screen
            deleteItemButton.setOnClickListener {
                val currentMenuItems = getMyMenuItems()
                currentMenuItems.remove(menuItem)
                val gson = Gson()
                val menuItems = gson.toJson(currentMenuItems)
                // Save updated MenuItems list to shared pref.
                requireActivity().getSharedPreferences("menu_items", Context.MODE_PRIVATE).edit().apply {
                    putString("menu_items", menuItems)
                }.apply()
                // Delete the saved NewItem in shared pref. in case it was the deleted one
                requireActivity().getSharedPreferences("new_item", Context.MODE_PRIVATE).edit().apply {
                    putString("new_item", "default value")
                }.apply()
                // Navigate back to main screen
                findNavController().navigate(R.id.action_menuItemInfo_to_FirstFragment)
            }

            // On click on EDIT go to EditItem screen
            editItemButton.setOnClickListener {
                val action = MenuItemInfoDirections.actionMenuItemInfoToEditItem(menuItem.id)
                findNavController().navigate(action)
            }
        }
    }
}
