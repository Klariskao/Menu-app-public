package com.example.myapplication.data

import com.example.myapplication.R

// Pre-defined Menu Items
class MenuItemData {

    var allMenuItems: MutableList<MenuItem> = mutableListOf(
        MenuItem("1", "Beef hamburger with fries", "£", 20.66, "Juicy ground beef patty on crunchy bun with freshly prepared homemade french fries", "Main", listOf("Beef"), listOf("Bread"), "", listOf("Gluten"), rating_total = 5.0, rated_by = 1, photo = "android.resource://com.example.myapplication/" + R.drawable.hamburger2),
        MenuItem("2", "Chicken and rice with creamy cheese sauce", "£", 30.06, "Chicken breast in creamy cheese sauce", "Main", listOf("Chicken"), listOf("Rice"), "", listOf("Milk"), rating_total = 4.5, rated_by = 1),
        MenuItem("3", "Duck with dumplings", "£", 40.00, "Delicious baked duck served with red cabbage and potato dumplings", "Main", listOf("Duck"), listOf("Dumplings"), "", listOf("Gluten"), rating_total = 4.0, rated_by = 1),
        MenuItem("4", "Smoked salmon with potatoes", "£", 50.00, "Fresh smoked salmon served with potatoes", "Main", listOf("Fish"), listOf("Potatoes"), "", listOf("Fish"), rating_total = 3.5, rated_by = 1),
        MenuItem("5", "Salad with mushroom", "£",10.00,  "Delicious and light vegan option", "Starter", listOf("Vegan"), listOf("Mushroom"), "", listOf(), rating_total = 2.2, rated_by = 1),
        MenuItem("6", "Red velvet cake", "£",201.00,  "Whole red velvet cake", "Dessert", listOf(), listOf(), "", listOf("Gluten", "Milk"), rating_total = 0.5, rated_by = 1),
        MenuItem(id="7", name="Shrimp pasta", currency="£", price=14.0, description="", category="Starter", meat=listOf("Seafood"), side=listOf("Pasta"), drink="", allergens=listOf("Celery", "Crustaceans", "Eggs", "Fish", "Gluten", "Sesame"), rated_by=0, rating_total=0.0),
        MenuItem(id="8", name="Traditional sausage with bread", currency="£", price=6.0, description="", category="Starter", meat=listOf("Beef", "Pork"), side=listOf("Bread"), drink="", allergens=listOf("Gluten", "Mustard", "Sulphur dioxide"), rated_by=0, rating_total=0.0),
        MenuItem(id="9", name="Chicken noodle soup", currency="£", price=5.0, description="", category="Soup", meat=listOf("Chicken"), side=listOf("Pasta"), drink="", allergens=listOf("Gluten"), rated_by=0, rating_total=0.0),
        MenuItem(id="10", name="Tomato soup", currency="£", price=5.0, description="", category="Soup", meat=listOf("Vegan", "Vegetarian"), side=listOf(), drink="", allergens=listOf(), rated_by=0, rating_total=0.0),
        MenuItem(id="11", name="Tiramisu", currency="£", price=5.0, description="One serving of the delicious Italian dessert", category="Dessert", meat=listOf("Vegetarian"), side=listOf(), drink="", allergens=listOf("Eggs", "Gluten", "Milk", "Nuts"), rated_by=0, rating_total=0.0),
        MenuItem(id="12", name="Coca cola", currency="£", price=3.0, description="", category="Drink", meat=listOf(), side=listOf(), drink="Soft", allergens=listOf(), rated_by=0, rating_total=0.0),
        MenuItem(id="13", name="Sprite", currency="£", price=3.0, description="", category="Drink", meat=listOf(), side=listOf(), drink="Soft", allergens=listOf(), rated_by=0, rating_total=0.0),
        MenuItem(id="14", name="Green tea", currency="£", price=3.0, description="", category="Drink", meat=listOf(), side=listOf(), drink="Hot", allergens=listOf("Sulphur dioxide"), rated_by=0, rating_total=0.0),
        MenuItem(id="15", name="Black tea", currency="£", price=3.0, description="", category="Drink", meat=listOf(), side=listOf(), drink="Hot", allergens=listOf(), rated_by=0, rating_total=0.0),
        MenuItem(id="16", name="Rum", currency="£", price=4.0, description="", category="Drink", meat=listOf(), side=listOf(), drink="Alcohol", allergens=listOf(), rated_by=0, rating_total=0.0),
        MenuItem(id="17", name="Corona", currency="£", price=2.0, description="", category="Drink", meat=listOf(), side=listOf(), drink="Beer", allergens=listOf(), rated_by=0, rating_total=0.0),
        MenuItem(id="18", name="Gin", currency="£", price=4.0, description="", category="Drink", meat=listOf(), side=listOf(), drink="Alcohol", allergens=listOf(), rated_by=0, rating_total=0.0),
        MenuItem(id="19", name="Red wine", currency="£", price=5.0, description="", category="Drink", meat=listOf(), side=listOf(), drink="Wine", allergens=listOf(), rated_by=0, rating_total=0.0),
        MenuItem(id="20", name="White wine", currency="£", price=5.0, description="", category="Drink", meat=listOf(), side=listOf(), drink="Wine", allergens=listOf(), rated_by=0, rating_total=0.0)
    )
}