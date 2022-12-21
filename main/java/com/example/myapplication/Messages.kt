package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class Messages : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)
        val id = intent.getIntExtra("id", 0)
        val databaseHandler = DatabaseHandler(this)
        val email = databaseHandler.getEmail(id)
        var subject: String
        var message: String
        findViewById<TextView>(R.id.email).text = email

        findViewById<ImageView>(R.id.goBack).setOnClickListener{
            finish()
        }
        findViewById<ImageView>(R.id.send).setOnClickListener {

            //subject of message
            subject = findViewById<EditText>(R.id.sbjText).text.toString()
            message = findViewById<EditText>(R.id.message).text.toString()

            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, message)

            intent.type = "message/rfc822"

            startActivity(Intent.createChooser(intent, "Choose email client: "))
        }

    }
}