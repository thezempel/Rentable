package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
//displays item available for rent
//includes the items picture, name, price and description
//allows user to message the owner and rent the time
class Rent : AppCompatActivity() {


    @SuppressLint("ResourceAsColor", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent)
        val databaseHandler = DatabaseHandler(this)
        val id = intent.getIntExtra("id",0)
        Log.d(TAG, "rent*****" + id)
        val name = intent.getStringExtra("name")
        val price = intent.getStringExtra("price")
        val description = intent.getStringExtra("description")
        val byteArray = intent.getByteArrayExtra("pic")
        val image: Bitmap? = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)


        findViewById<TextView>(R.id.itemName).text = name
        findViewById<TextView>(R.id.description).text = "DESCRIPTION:\n\n" + description
        findViewById<TextView>(R.id.price).text = "PRICE $" + price

        //convert back into bitmap
        findViewById<ImageView>(R.id.picture).setImageBitmap(image)

        findViewById<Button>(R.id.back).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.message).setOnClickListener {
            //Toast.makeText(applicationContext,"not done...probably wont be", Toast.LENGTH_LONG ).show()
            val intent = Intent(this@Rent, Messages::class.java)

            //put extras
            intent.putExtra("id", id)
            Log.d(TAG, "*****rent: " + id)

            startActivity(intent)

        }

        findViewById<Button>(R.id.rent).setOnClickListener {
            if(id != null) {
                if (databaseHandler.checkRented(id)) {//if is true
                    findViewById<Button>(R.id.rent).error = "Item is already rented"
                }
                else {
                    databaseHandler.rent(id)
                    Toast.makeText(applicationContext, "Rented", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

    }





}