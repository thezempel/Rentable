package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class About : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_page)
        findViewById<ImageView>(R.id.goBack).setOnClickListener{
            finish()
        }
        findViewById<TextView>(R.id.info).text = "Rentable is an item renting platform - a place to rent that item you need, " +
                "or earn some extra cash on items you have just sitting around!\n\n" +
                "Your dashboard displays all the items you have rented\n" +
                "By clicking on each item, the time it has been rented for will be displayed at the top\n\n" +
                "In the navigation menu,\n" +
                "Click the 'search' tab to search for any item you can think of!\n" +
                "You can click the search icon to display everything that is up for rent, \n" +
                "or search for that specific item you need\n\n" +
                "The 'My Items' tab displays all of the items you have up for lease\n\n" +
                "Hit 'Add Item' to put another item up for rent\n\n\n" +
                "Happy renting!"
    }
}