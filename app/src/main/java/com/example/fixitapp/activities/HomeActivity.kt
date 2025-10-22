package com.example.fixitapp.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fixitapp.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 👉 Recibir el correo desde el Intent
        val usuario = intent.getStringExtra("usuario")

        // 👉 Mostrarlo en el TextView (debes tener un TextView en tu XML)
        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)
        tvBienvenida.text = "Bienvenido, $usuario 👋"
    }
}
