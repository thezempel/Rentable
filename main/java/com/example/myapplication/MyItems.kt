/*
basis of code based on code from:
https://tutorials.eu/android-sqlite-database-in-depth-guide/#ftoc-heading-22
modified by Jonathon Zempel
 */

package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityDashboardBinding
import com.example.myapplication.databinding.ActivityLeasedItemBinding
import java.io.ByteArrayOutputStream
import java.util.ArrayList

class MyItems : AppCompatActivity() {
    private lateinit var itemList: ArrayList<ItemModelClass>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_items)
        setupListofDataIntoRecyclerView()
        findViewById<ImageView>(R.id.goBack).setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
        }
    }

    //gets list of user items up for lease
    private fun getItemsList(): ArrayList<ItemModelClass> {
        val databaseHandler = DatabaseHandler(this)
        itemList = databaseHandler.viewLeasedItems()
        return itemList
    }
    //connects list of items to the recyclerview to be displayed
    private fun setupListofDataIntoRecyclerView() {
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerView)
        getItemsList()
        if(itemList.size > 0) {
            recyclerview.visibility = View.VISIBLE
            findViewById<TextView>(R.id.noItems).visibility = View.GONE

            recyclerview.layoutManager = LinearLayoutManager(this)//connects the recyclerview

            val clickAdapter = ItemAdapter(this, itemList)//allows each item to be clicked on
            recyclerview.adapter = clickAdapter

            clickAdapter.setOnItemClickListener(object: ItemAdapter.onItemClickListener {
                //clicky clicky
                override fun onItemClick(position: Int) {
                    Log.d(ContentValues.TAG, "******" + itemList[position].description)
                    val intent = Intent(this@MyItems, LeasedItems::class.java)
                    //bitmap -> bytearray
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