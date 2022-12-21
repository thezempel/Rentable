/*
basis of code based on code from:
https://tutorials.eu/android-sqlite-database-in-depth-guide/#ftoc-heading-22
modified by Jonathon Zempel
 */

package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.text.Layout
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityDashboardBinding
//import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import java.text.ParseException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalDateTime.*
import java.time.Period
import java.util.*
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.time.Duration

class Dashboard : AppCompatActivity() {
    private lateinit var itemList: ArrayList<ItemModelClass>
    val databaseHandler = DatabaseHandler(this)

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setupListofDataIntoRecyclerView()

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        findViewById<ImageView>(R.id.menu_button).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        //make each thing clickable - attach to individual classes/layouts
        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.logout -> startActivity(Intent(this, Login::class.java))
                R.id.add-> startActivity(Intent(this, AddItem::class.java))
                R.id.search -> startActivity(Intent(this, Search::class.java))
                R.id.myItems -> startActivity(Intent(this, MyItems::class.java))
                R.id.about -> startActivity(Intent(this, About::class.java))
            }
            true
        }

    }


    //gets list of rented items
    private fun getItemsList(): ArrayList<ItemModelClass> {
        itemList = databaseHandler.viewRentedItems()
        Log.d(TAG, "*****requesting database")
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
                //picks out the id of which item is clicked on - access database to get the time
                override fun onItemClick(position: Int) {
                    Log.d(TAG, "******" + itemList[position].description)
                    val id = itemList[position].id
                    getTime(id!!)
                }
            })
        }
        else {
            recyclerview.visibility = View.GONE
            findViewById<TextView>(R.id.noItems).visibility= View.VISIBLE
        }
    }
    //gets the time rented from the database, compares it to current time, and returns the difference
    private fun getTime(id: Int) {

        var time = databaseHandler.getTime(id)//time stored
        var current = System.currentTimeMillis()/1000//current unix time
        val diff = current - time//in seconds

        Log.d(TAG, "" + time + "******")
        Log.d(TAG, "" + current + "^^^^^")
        Log.d(TAG, "" + diff + "****")

        val days = diff / 86400
        val hours = diff % 86400 / 3600
        val minutes = diff % 3600 / 60

        var dispDays: String
        var dispHours: String
        var dispMinutes: String

        if(days < 1) dispDays = " \n"
        else if (days > 1) dispDays = "$days days\n"
        else dispDays = "$days day\n"

        if(hours < 1) dispHours = " \n"
        else if(hours > 1) dispHours = "$hours hours\n"
        else dispHours = "$hours hour\n"

        if(minutes < 1) dispMinutes = " \n"
        else if(minutes > 1) dispMinutes = "$minutes minutes"
        else dispMinutes = "$minutes minute"

        var dispTimer = dispDays + dispHours + dispMinutes

        findViewById<TextView>(R.id.timeDisplay).textSize = 24.0f
        findViewById<TextView>(R.id.timeDisplay).text = dispTimer


    }


}