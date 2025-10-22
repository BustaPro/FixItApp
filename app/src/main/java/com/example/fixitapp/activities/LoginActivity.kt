package com.example.fixitapp.activities

import android.content.Intent
import android.widget.Button
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import com.example.fixitapp.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val btnGoToRegister = findViewById<Button>(R.id.btnGoToRegister)

        btnGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}