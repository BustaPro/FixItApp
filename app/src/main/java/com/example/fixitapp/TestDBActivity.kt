package com.example.fixitapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fixitapp.data.DatabaseHelper
import com.example.fixitapp.model.Service

class TestDBActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DatabaseHelper(this)

        // Creamos un servicio de prueba
        val testService = Service(
            id = 0,
            nombreCliente = "Bastián Bustamante",
            tipoServicio = "Reparación TV",
            fecha = "23/10/2025",
            descripcion = "El televisor no enciende",
            estado = "Pendiente"
        )

        val insertado = db.addService(testService)

        if (insertado) {
            Toast.makeText(this, "✅ Servicio insertado correctamente", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "❌ Error al insertar servicio", Toast.LENGTH_SHORT).show()
        }

        // Recuperamos todos los servicios
        val lista = db.getAllServices()

        if (lista.isNotEmpty()) {
            val mensaje = "📋 Total servicios: ${lista.size}\nPrimero: ${lista[0].tipoServicio}"
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "⚠️ No se encontraron servicios", Toast.LENGTH_SHORT).show()
        }
    }
}
