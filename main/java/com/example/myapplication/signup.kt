package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class signup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        findViewById<Button>(R.id.button).setOnClickListener {
            signUpUser()

            }
        findViewById<TextView>(R.id.back).setOnClickListener {
            finish()
            }
        }


    @SuppressLint("SuspiciousIndentation")
    private fun signUpUser() {
        val intent = Intent(this, Login::class.java)
        if (findViewById<TextView>(R.id.firstName).text.toString().isEmpty()) {
            findViewById<TextView>(R.id.firstName).error = "Please enter you first name"
            findViewById<TextView>(R.id.firstName).requestFocus()
            return
        }

        if (findViewById<TextView>(R.id.lastName).text.toString().isEmpty()) {
            findViewById<TextView>(R.id.lastName).error = "Please enter your last name"
            findViewById<TextView>(R.id.lastName).requestFocus()
            return
        }

        if (findViewById<TextView>(R.id.userName).text.toString().isEmpty()) {
            findViewById<TextView>(R.id.userName).error = "Please enter a username"
            findViewById<TextView>(R.id.userName).requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(findViewById<TextView>(R.id.email).text.toString())
                .matches()) {
            if (findViewById<TextView>(R.id.email).text.toString().isEmpty()) {
                findViewById<TextView>(R.id.email).error = "Please enter valid email"
                findViewById<TextView>(R.id.email).requestFocus()
                return
            }
        }
        if (findViewById<TextView>(R.id.street).text.toString().isEmpty()) {
            findViewById<TextView>(R.id.street).error = "Please enter street"
            findViewById<TextView>(R.id.street).requestFocus()
            return
        }
        if (findViewById<TextView>(R.id.city).text.toString().isEmpty()) {
            findViewById<TextView>(R.id.city).error = "Please enter city"
            findViewById<TextView>(R.id.city).requestFocus()
            return
        }
        if (findViewById<TextView>(R.id.state).text.toString().isEmpty()) {
            findViewById<TextView>(R.id.state).error = "Please enter state"
            findViewById<TextView>(R.id.state).requestFocus()
            return
        }
        if (findViewById<TextView>(R.id.zipcode).text.toString().isEmpty()) {
            findViewById<TextView>(R.id.zipcode).error = "Please enter zip code"
            findViewById<TextView>(R.id.zipcode).requestFocus()
            return
        }
        if (findViewById<TextView>(R.id.password).text.toString().isEmpty()) {
            findViewById<TextView>(R.id.password).error = "Please enter password"
            findViewById<TextView>(R.id.password).requestFocus()
            return
        }
        if (findViewById<TextView>(R.id.password).text.toString() != findViewById<TextView>(R.id.confirmPass).text.toString()) {
            findViewById<TextView>(R.id.confirmPass).error = "Passwords don't match"
            findViewById<TextView>(R.id.confirmPass).requestFocus()
            return

        }
        if(!findViewById<CheckBox>(R.id.checkBox).isChecked) {
            findViewById<CheckBox>(R.id.checkBox).error = "please accept terms"
            findViewById<CheckBox>(R.id.checkBox).requestFocus()
            return
        }

        val name = findViewById<TextView>(R.id.firstName).text.toString() + " " + findViewById<TextView>(R.id.lastName).text.toString()
        val username = findViewById<TextView>(R.id.userName).text.toString()
        val email = findViewById<TextView>(R.id.email).text.toString()
        val address = findViewById<TextView>(R.id.street).text.toString() +
                        " " + findViewById<TextView>(R.id.city).text.toString() +
                        " " + findViewById<TextView>(R.id.state).text.toString() +
                        " " + findViewById<TextView>(R.id.zipcode).text.toString()
        val password = findViewById<TextView>(R.id.confirmPass).text.toString()
        val databaseHandler = DatabaseHandler(this)
        databaseHandler.addUser(UserModelClass(name, username, email, address, password))

            startActivity(intent)
    }
}