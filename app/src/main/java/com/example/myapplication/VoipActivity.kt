package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class VoipActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.videoBtn).setOnClickListener {
           /* val intent = Intent(this, Video::class.java)
            startActivity(intent)*/
        }

        findViewById<Button>(R.id.voiceBtn).setOnClickListener {
            val intent = Intent(this, Voice::class.java)
            startActivity(intent)
        }
    }
}