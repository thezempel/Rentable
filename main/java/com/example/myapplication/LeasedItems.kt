/*
basis of code based on code from:
https://tutorials.eu/android-sqlite-database-in-depth-guide/#ftoc-heading-22
Modified by Jonathon Zempel
 */

package com.example.myapplication


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button

import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//displays the item that the user has put up for rent
//displays the picture, name, price, and description
//allows user to remove item from listing
class LeasedItems: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leased_item)
        val databaseHandler = DatabaseHandler(this)

        val id = intent.getIntExtra("id",0)
        val name = intent.getStringExtra("name")
        val price = intent.getStringExtra("price")
        val description = intent.getStringExtra("description")
        val byteArray = intent.getByteArrayExtra("pic")
        val image: Bitmap? = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)//for image - convert bytearray to bitmap

        findViewById<TextView>(R.id.itemName).text = name
        findViewById<TextView>(R.id.description).text = "DESCRIPTION:\n\n" + description
        findViewById<TextView>(R.id.price).text = "PRICE $" + price
        findViewById<ImageView>(R.id.picture).setImageBitmap(image)

        //rented?
            if (databaseHandler.checkRented(id)) {
                findViewById<TextView>(R.id.isRented).text = "RENTED"
                findViewById<Button>(R.id.returnItem).visibility = View.VISIBLE
            }
            else
                findViewById<TextView>(R.id.isRented).text = "not rented"


        //back button
        findViewById<ImageView>(R.id.goBack).setOnClickListener {
            finish()
        }
        //delete button
        findViewById<Button>(R.id.delete).setOnClickListener {
            findViewById<Button>(R.id.delete).visibility = View.GONE
            findViewById<Button>(R.id.confirmDelete).visibility = View.VISIBLE
        }
            findViewById<Button>(R.id.confirmDelete).setOnClickListener {
                    databaseHandler.removeItem(id)

                Toast.makeText(applicationContext, "Item has been deleted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MyItems::class.java))
            }
        //return item
        findViewById<Button>(R.id.returnItem).setOnClickListener {
            databaseHandler.returnItem(id)
            findViewById<TextView>(R.id.isRented).text = "not rented"
            findViewById<Button>(R.id.returnItem).visibility = View.GONE
        }
    }
}