/*
basis of code based on code from:
https://tutorials.eu/android-sqlite-database-in-depth-guide/#ftoc-heading-22
modified by Jonathon Zempel
 */

package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.ByteArrayOutputStream
import java.util.ArrayList

class Search : AppCompatActivity() {
    private lateinit var itemList: ArrayList<ItemModelClass>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        itemList = ArrayList<ItemModelClass>()

        findViewById<ImageView>(R.id.searchBtn).setOnClickListener {
            val search = findViewById<EditText>(R.id.search).text.toString()
            if(findViewById<EditText>(R.id.search).text.toString().isEmpty()) {
                setupListofDataIntoRecyclerView()
            }
            else {
            setupListofDataIntoRecyclerView(search) }
        }

        findViewById<ImageView>(R.id.goBack).setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
        }
    }

    //accesses database to get the list of all of the items stored
    private fun getItemsList(): ArrayList<ItemModelClass> {
        val databaseHandler = DatabaseHandler(this)
        itemList = databaseHandler.viewItems()//new
        Log.d(TAG, "******ALL")
        return itemList
    }
    //takes the list of items, and inserts each item into the recycler view
    //uses compact_view layout
    private fun setupListofDataIntoRecyclerView() {
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        getItemsList()
        //displays items, if there are any
        if(itemList.size > 0) {
            recyclerview.visibility = View.VISIBLE
            findViewById<TextView>(R.id.noItems).visibility = View.GONE//removes no items text
            recyclerview.layoutManager = LinearLayoutManager(this)//hooks up the recycler layout to search layout
            //allows items to be clicked on
            val clickAdapter = ItemAdapter(this, itemList)
            recyclerview.adapter = clickAdapter

            clickAdapter.setOnItemClickListener(object: ItemAdapter.onItemClickListener {
                //allows for each item to be clicked on,sending the info to Rent class and layout
                override fun onItemClick(position: Int) {
                Log.d(TAG, "ALL******" + itemList[position].id)
                    val intent = Intent(this@Search, Rent::class.java)
                    //convert bitmap to bytearray
                    val stream = ByteArrayOutputStream()
                    itemList[position].pic.compress(Bitmap.CompressFormat.PNG, 90, stream)
                    val image = stream.toByteArray()
                    //put extras
                    intent.putExtra("id", itemList[position].id)
                    intent.putExtra("name", itemList[position].name)
                    intent.putExtra("description", itemList[position].description)
                    intent.putExtra("price", itemList[position].price)
                    intent.putExtra("pic", image)

                    //intent.putExtra("isRented", itemList[position].isRented)
                    startActivity(intent)
                }

            })
        //end new
        }
        else {
            recyclerview.visibility = View.GONE
            findViewById<TextView>(R.id.noItems).visibility= View.VISIBLE
        }
    }//overloaded to allow a specific search
    private fun getItemsList(search: String): ArrayList<ItemModelClass> {
        val databaseHandler = DatabaseHandler(this)
        itemList = databaseHandler.search(search)
        Log.d(TAG, "******search")
        return itemList
    }

    //overloaded for the specific search
    private fun setupListofDataIntoRecyclerView(search: String) {
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        getItemsList(search)
        if(itemList.size > 0) {
            recyclerview.visibility = View.VISIBLE
            findViewById<TextView>(R.id.noItems).visibility = View.GONE
            recyclerview.layoutManager = LinearLayoutManager(this)
            val clickAdapter = ItemAdapter(this, itemList)
            recyclerview.adapter = clickAdapter
            clickAdapter.setOnItemClickListener(object: ItemAdapter.onItemClickListener {

                override fun onItemClick(position: Int) {
                    Log.d(TAG, "search******" + itemList[position].id)
                    val intent = Intent(this@Search, Rent::class.java)
                    //convert bitmap to bytearray
                    val stream = ByteArrayOutputStream()
                    itemList[position].pic.compress(Bitmap.CompressFormat.PNG, 90, stream)
                    val image = stream.toByteArray()
                    //put extras
                    intent.putExtra("id", itemList[position].id)
                    intent.putExtra("name", itemList[position].name)
                    intent.putExtra("description", itemList[position].description)
                    intent.putExtra("price", itemList[position].price)
                    intent.putExtra("pic", image)

                    startActivity(intent)
                }

            })
        }
        else {
            recyclerview.visibility = View.GONE
            findViewById<TextView>(R.id.noItems).visibility= View.VISIBLE
        }
    }
}