package com.example.fixitapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fixitapp.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Obtenemos el correo del usuario enviado desde LoginActivity
        val usuario = intent.getStringExtra("usuario")

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        tvWelcome.text = "Bienvenido, $usuario 👋"

        // Acción del botón de cerrar sesión
        btnLogout.setOnClickListener {
            // Volver al LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Cerramos HomeActivity para que no pueda volver atrás
        }
    }
}
