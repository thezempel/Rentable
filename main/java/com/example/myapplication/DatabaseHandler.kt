
package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import java.io.ByteArrayOutputStream


class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "Rentable"

        //item table
        private val TABLE_ITEMS = "Items"
        private val KEY_ID = "id"
        val KEY_ITEM_NAME = "item"
        val KEY_DESC = "description"
        val KEY_PRICE = "price"
        val KEY_ISRENTED = "isRented"
        val KEY_RENT = "renter"//renters username
        val KEY_LEASE = "leaser"//leasers username
        val KEY_TIME = "time"
        val KEY_PIC = "pic"

        //user table
        private val TABLE_USERS = "Users"
        private val KEY_USERNAME = "username"
        val KEY_NAME = "name"
        val KEY_EMAIL = "email"
        val KEY_ADDRESS = "address"
        val KEY_PASSWORD = "password"


    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "***CREATE TABLE***")
        val table_query = ("CREATE TABLE " + TABLE_ITEMS + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_ITEM_NAME + " TEXT,"
                + KEY_DESC +" TEXT,"
                + KEY_PRICE +" TEXT, "
                + KEY_ISRENTED + " NUMERIC, "
                + KEY_RENT + " TEXT, "
                + KEY_LEASE + " TEXT, "
                + KEY_TIME + " INT, "
                + KEY_PIC + " BLOB"
                + ")" )

        val user_query = ("CREATE TABLE " + TABLE_USERS + " (" + KEY_USERNAME + " TEXT PRIMARY KEY, "
                + KEY_NAME + " TEXT, "
                + KEY_EMAIL + " TEXT, "
                + KEY_ADDRESS + " TEXT, "
                + KEY_PASSWORD + " TEXT"
                + ")")

        db.execSQL(table_query)
        db.execSQL(user_query)



    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS)
        onCreate(db)
    }
    //add new item into database, uses the ItemModelClass object to pass values
    fun addItem(item: ItemModelClass){
        Log.d(TAG, "inside addItem")
        val db = this.writableDatabase
        val pic = item.pic
        //have to change bitmap into bytearray to store in db
        val stream = ByteArrayOutputStream()
        pic.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()
        val contentValues = ContentValues()
        contentValues.put(KEY_ITEM_NAME, item.name)
        contentValues.put(KEY_DESC, item.description)
        contentValues.put(KEY_PRICE, item.price)
        contentValues.put(KEY_ISRENTED, 0)//set to false
        contentValues.put(KEY_LEASE, Login.user)//user taken from Login
        contentValues.put(KEY_PIC, image)

        db.insert(TABLE_ITEMS, null, contentValues)
        db.close()


    }

    //remove item
    @SuppressLint("SuspiciousIndentation")
    fun removeItem(id: Int){
        val db = this.writableDatabase
        val query = "DELETE FROM " + TABLE_ITEMS +
                " WHERE " + KEY_ID + " = $id"
                " AND Leaser = " + "'" + Login.user +"'"
        db.execSQL(query)
        db.close()
    }

    //get the full list of items
    @SuppressLint("Range")
    fun viewItems(): ArrayList<ItemModelClass> {
        val itemList: ArrayList<ItemModelClass> = ArrayList<ItemModelClass>()
        //query get all
        val selectQuery = "SELECT * FROM $TABLE_ITEMS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery,null)
        } catch(e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var name: String
        var description: String
        var price: String
        var id: Int
        var pic: ByteArray

        if(cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME))
                description = cursor.getString(cursor.getColumnIndex(KEY_DESC))
                price = cursor.getString(cursor.getColumnIndex(KEY_PRICE))
                pic = cursor.getBlob(cursor.getColumnIndex(KEY_PIC))

                //now need to change BACK into bitmap
                val bmp = BitmapFactory.decodeByteArray(pic, 0, pic.size)
                //attach values into ItemModelClass object
                val item = ItemModelClass(id = id, name = name, description = description, price = price, pic = bmp)
                itemList.add(item)
            } while (cursor.moveToNext())
        }
        db.close()
        close()
        return itemList
    }
    //gets list of items rented by the user
    @SuppressLint("Range")
    fun viewRentedItems() :ArrayList<ItemModelClass> {
        val rentedList: ArrayList<ItemModelClass> = ArrayList<ItemModelClass>()
        //get items attached to username that have been rented
        val rentQuery = "SELECT * FROM " + TABLE_ITEMS + " WHERE renter = " + "'" + Login.user + "'" +
                " ORDER BY " + KEY_TIME + " DESC"
        val db = this.readableDatabase
        Log.d(TAG, rentQuery)
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(rentQuery,null)

        } catch(e: SQLiteException) {
            db.execSQL(rentQuery)
            return ArrayList()
        }
        var id: Int
        var name: String
        var description: String
        var price: String
        var pic: ByteArray

        if(cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME))
                description = cursor.getString(cursor.getColumnIndex(KEY_DESC))
                price = cursor.getString(cursor.getColumnIndex(KEY_PRICE))
                pic = cursor.getBlob(cursor.getColumnIndex(KEY_PIC))

                //bytearray -> bitmap
                val bmp = BitmapFactory.decodeByteArray(pic, 0, pic.size)
                //attach to ItemModelClass object
                val item = ItemModelClass(id = id, name = name, description = description, price = price, pic = bmp)
                rentedList.add(item)
            } while (cursor.moveToNext())
        }
        db.close()
        close()
        return rentedList
    }

    //add new user
    fun addUser(user: UserModelClass) {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_USERNAME, user.userName)
        contentValues.put(KEY_NAME, user.name)
        contentValues.put(KEY_EMAIL, user.email)
        contentValues.put(KEY_ADDRESS, user.address)
        contentValues.put(KEY_PASSWORD, user.password)

        db.insert(TABLE_USERS, null, contentValues)

        db.close()

    }
    //verify login password is correct
    @SuppressLint("Range")
    fun checkLogin(pass: String): String {
        //query gets password attached to username
        val query = "SELECT " + KEY_PASSWORD + " FROM " + TABLE_USERS +
                " WHERE " + KEY_USERNAME + " = '" + Login.user + "'"
        Log.d(TAG, " ^^^^^^" + Login.user)
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(query,null)

        } catch(e: SQLiteException) {
            db.execSQL(query)
        }
        //gibberish
        var pwdCheck = "werflkh5334gnal;dfkn"

        if (cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    pwdCheck = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD))
                    Log.d(TAG, pass + "******" + pwdCheck)
                } while (cursor.moveToNext())
            }
        }
        close()
        db.close()
        return pwdCheck

    }
    //gets the list of items the user has put up for lease
    @SuppressLint("Range")
    fun viewLeasedItems(): ArrayList<ItemModelClass> {
        val itemList: ArrayList<ItemModelClass> = ArrayList<ItemModelClass>()
        //query get all items the user has put up for lease
        val selectQuery = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + KEY_LEASE + " = '" + Login.user + "'"
        Log.d(TAG, " *****" + Login.user)
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery,null)

        } catch(e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var description: String
        var price: String
        var pic: ByteArray

        if(cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME))
                description = cursor.getString(cursor.getColumnIndex(KEY_DESC))
                price = cursor.getString(cursor.getColumnIndex(KEY_PRICE))
                pic = cursor.getBlob(cursor.getColumnIndex(KEY_PIC))

                //change back to bitmap
                val bmp = BitmapFactory.decodeByteArray(pic, 0, pic.size)
                //create ItemModelClass object
                val item = ItemModelClass(id = id, name = name, description = description, price = price, pic = bmp)
                itemList.add(item)
            } while (cursor.moveToNext())
        }
        db.close()
        close()
        return itemList
    }
    //s
    @SuppressLint("Range")
    fun search(item: String): ArrayList<ItemModelClass> {
        val searchList: ArrayList<ItemModelClass> = ArrayList<ItemModelClass>()
        //query items that start with search param
        val searchQuery = "SELECT * FROM " + TABLE_ITEMS + " WHERE " + KEY_ITEM_NAME + " LIKE '" + item + "%'"//FIXME
        Log.d(TAG, "*******" + item)
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(searchQuery,null)
            Log.d(TAG, "******" + searchQuery)
        } catch(e: SQLiteException) {
            db.execSQL(searchQuery)
            return ArrayList()
        }
        var id: Int
        var name: String
        var description: String
        var price: String
        var pic: ByteArray


        if(cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_ITEM_NAME))
                description = cursor.getString(cursor.getColumnIndex(KEY_DESC))
                price = cursor.getString(cursor.getColumnIndex(KEY_PRICE))
                pic = cursor.getBlob(cursor.getColumnIndex(KEY_PIC))
                //bytearry -> bitmap
                val bmp = BitmapFactory.decodeByteArray(pic, 0, pic.size)
                //ItemModelClass object
                val item = ItemModelClass(id = id, name = name, description = description, price = price, pic = bmp)
                searchList.add(item)
            } while (cursor.moveToNext())
        }
        db.close()
        close()
        return searchList

    }
    //attaches username to Renter key in db, sets the current time to Time key, changes rented bool to true
    @SuppressLint("SuspiciousIndentation")
    fun rent(id: Int) {
        val db = this.writableDatabase
        //query - renter = user, time = current, isRented = true
        val query = "UPDATE " + TABLE_ITEMS +
                    " SET " + KEY_RENT + " = " + "'" + Login.user + "', " +
                    KEY_ISRENTED + " = 1, " + KEY_TIME + " = (strftime('%s', 'now')) " +
                    "WHERE " + KEY_ID + " = $id"
        Log.d(TAG, query)
        db.execSQL(query)
        db.close()
    }
    //checks to see if a leased item is rented or not
    @SuppressLint("Range", "SuspiciousIndentation")
    fun checkRented(id: Int): Boolean {
        var isRented = false
        val db = this.readableDatabase
        //query - check if rented by id
        val query = "SELECT " + KEY_ISRENTED + " FROM " + TABLE_ITEMS +
                    " WHERE " + KEY_ID + " = $id"

        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(query,null)

        } catch(e: SQLiteException) {
            db.execSQL(query)
            return true
        }
        if(cursor.moveToFirst()) {
            do {
                if(cursor.getInt(cursor.getColumnIndex(KEY_ISRENTED)) == 1) isRented = true

            } while (cursor.moveToNext())
        }
        close()
        db.close()
        return isRented
    }
    //returns the email based off items id
    @SuppressLint("Range")
    fun getEmail(id: Int): String{
        Log.d(TAG, "*****trying to get email")
        var email= ""
        val db = this.readableDatabase
        //query - get email for username attached to items id
        val query = "SELECT " + KEY_EMAIL + " FROM " + TABLE_USERS +
                " WHERE " + KEY_USERNAME +
                " = (SELECT " + KEY_LEASE + " FROM " + TABLE_ITEMS + " WHERE " + KEY_ID + " = $id)"
        Log.d(TAG, query)
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(query,null)

        } catch(e: SQLiteException) {
            db.execSQL(query)
        }
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL))
                    Log.d(TAG, "******" + email)
                } while (cursor.moveToNext())
            }
        }
        close()
        db.close()
        return email
    }
    //get the time it was rented that is stored in Time key by item id
    @SuppressLint("Range")
    fun getTime(id: Int): Int {
        val db = this.readableDatabase
        var timeString = ""
        //query - get time of item id
        val query = "SELECT " + KEY_TIME + " FROM " + TABLE_ITEMS +
                " WHERE " + KEY_ID + " = $id"

        Log.d(TAG, query)
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(query,null)

        } catch(e: SQLiteException) {
            db.execSQL(query)
        }
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    timeString = cursor.getString(cursor.getColumnIndex(KEY_TIME))
                    Log.d(TAG, "******" + timeString)
                } while (cursor.moveToNext())
            }
        }
        close()
        db.close()
        val new = timeString.toInt()//comes out as a string, so lets change it to an int
        Log.d(TAG, "^^^^^^" + new)
        return new
    }

    //allows leaser to mark item as returned
    fun returnItem(id: Int) {
        val db = this.writableDatabase
        //query clears name from renter, sets isRented to false, and clears Time
        val query = "UPDATE " + TABLE_ITEMS +
                " SET " + KEY_RENT + " = " + "'null', " +
                KEY_ISRENTED + " = 0, " + KEY_TIME + " = 'null' " +
                "WHERE " + KEY_ID + " = $id"
        Log.d(TAG, query)
        db.execSQL(query)
        db.close()
    }
}