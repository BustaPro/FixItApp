package com.example.fixitapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fixitapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos el binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtenemos el correo del usuario enviado desde LoginActivity
        val usuario = intent.getStringExtra("usuario")
        binding.tvWelcome.text = "Bienvenido, $usuario 👋"

        // Botón de cerrar sesión
        binding.btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Cierra esta activity
        }

        // Botón para ver lista de servicios
        binding.btnVerServicios.setOnClickListener {
            val intent = Intent(this, ServiceListActivity::class.java)
            startActivity(intent)
        }
    }
}

