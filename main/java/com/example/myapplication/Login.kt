package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView




class Login : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

     //login
        findViewById<Button>(R.id.login).setOnClickListener {
           login()
        }
        //signup
        findViewById<Button>(R.id.signup).setOnClickListener {
            startActivity(Intent(this, signup::class.java))

        }

    }
    //for username purposes within app
    companion object {
        @JvmStatic
        lateinit var user: String
    }

    //logs in to app
    //when login button is clicked, makes sure there is a username and password
    //otherwise throws an error
    //then send the password to be verified
    private fun login() {
        if(!findViewById<EditText>(R.id.username).text.toString().isEmpty() &&
            !findViewById<EditText>(R.id.password).text.toString().isEmpty()) {
            user = findViewById<EditText>(R.id.username).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            //skips password for testing
            if(password == "skippassword") startActivity(Intent(this, Dashboard::class.java))


            verify(password)
        }
        else {

            findViewById<TextView>(R.id.password).error = "incorrect username or password"
            findViewById<TextView>(R.id.password).requestFocus()
        }
    }

    //verifies the password against the database
    private fun verify(pass: String) {
        val pwd = pass
        val databaseHandler = DatabaseHandler(this)
        val pwdCheck = databaseHandler.checkLogin(pass)
        Log.d(TAG, pwd + "****" + pwdCheck)
        if(pwdCheck == pass) {

            startActivity(Intent(this, Dashboard::class.java))
        }
        else {
            findViewById<TextView>(R.id.password).error = "incorrect username or password"
            findViewById<TextView>(R.id.password).requestFocus()
        }

    }
}