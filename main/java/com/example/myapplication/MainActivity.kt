package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.os.HandlerCompat.postDelayed
import java.util.*
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startLogin()
        }
    fun startLogin() {
        if(!isDestroyed) {
            val intent = Intent(this,Login::class.java)
            val timetask = timerTask {
                if(!isDestroyed) {
                    startActivity(intent)
                    finish()
                }
            }
            val timer = Timer()
            timer.schedule(timetask, 2000)

        }
    }

}