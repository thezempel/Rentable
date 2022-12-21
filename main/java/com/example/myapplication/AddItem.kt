/*
code for getting photo from user is based off of tutorial from:
https://github.com/turtlecode/AddPhotoFromGalleryKotlinAndroidTutorial/blob/main/app/src/main/java/com/turtlecode/addphotofromgallery_kotlinandroidtutorial/MainActivity.kt
 */

package com.example.myapplication

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AddItem : AppCompatActivity() {

    var userPic: Uri? = null//for picture
    var picBitmap: Bitmap? = null//for picture
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        findViewById<ImageView>(R.id.picture).setOnClickListener {
            pickPhoto()
        }

        findViewById<Button>(R.id.add).setOnClickListener {
            addNewItem()
        }

        findViewById<Button>(R.id.cancel).setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
        }
    }


    //allows user to set up a new item to lease
    private fun addNewItem() {
        val name = findViewById<TextView>(R.id.itemName).text.toString()
        val description = findViewById<TextView>(R.id.description).text.toString()
        val price = findViewById<TextView>(R.id.price).text.toString()
        val databaseHandler = DatabaseHandler(this)

        if(!name.isEmpty() && !description.isEmpty() && !price.isEmpty() && picBitmap!=null) {//
            Log.d(TAG, "trying to add")
            databaseHandler.addItem(ItemModelClass(null,
                findViewById<TextView>(R.id.itemName).text.toString(),
                findViewById<TextView>(R.id.description).text.toString(),
                findViewById<TextView>(R.id.price).text.toString(), picBitmap!!))//to add pic
            Toast.makeText(applicationContext, "added!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Dashboard::class.java))
        }
        else {
            if(name.isEmpty()) {
                findViewById<TextView>(R.id.itemName).error = "Item needs a name"
            }
            if(description.isEmpty()) {
                findViewById<TextView>(R.id.description).error = "please enter a description"
            }
            if(price.isEmpty()) {
                findViewById<TextView>(R.id.price).error = "item needs a price"
            }

            //Toast.makeText(applicationContext, "error", Toast.LENGTH_SHORT)
        }
    }
    //when adding a pic, allows the app to access devices storage, so user can upload their own picture
    fun pickPhoto() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }
        else {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gallery,2)
        }
    }
    //needed for permission granting - app cannot access user photos without getting permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gallery,2)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    //when picture is selected - turns photo into bitmap and attaches it as the layouts picture
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            userPic = data.data
            //changes user file to bitmap
            if (SDK_INT >= 28) {//if file size is too big
                val source = ImageDecoder.createSource(this.contentResolver, userPic!!)
                picBitmap = ImageDecoder.decodeBitmap(source)
                findViewById<ImageView>(R.id.picture).setImageBitmap(picBitmap)
            }
            else {
                picBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, userPic)
                findViewById<ImageView>(R.id.picture).setImageBitmap(picBitmap)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}