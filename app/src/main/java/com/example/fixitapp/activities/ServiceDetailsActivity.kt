package com.example.fixitapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fixitapp.R
import com.example.fixitapp.data.DatabaseHelper

class ServiceDetailsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_details)

        dbHelper = DatabaseHelper(this)

        val tvNombre = findViewById<TextView>(R.id.tvNombreClienteDetail)
        val tvTipo = findViewById<TextView>(R.id.tvTipoServicioDetail)
        val tvFecha = findViewById<TextView>(R.id.tvFechaDetail)
        val tvDescripcion = findViewById<TextView>(R.id.tvDescripcionDetail)
        val tvEstado = findViewById<TextView>(R.id.tvEstadoDetail)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        val serviceId = intent.getIntExtra("service_id", -1)
        if (serviceId == -1) {
            Toast.makeText(this, "Error: servicio no encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val service = dbHelper.getAllServices().find { it.id == serviceId }

        if (service != null) {
            tvNombre.text = service.nombreCliente
            tvTipo.text = service.tipoServicio
            tvFecha.text = service.fecha
            tvDescripcion.text = service.descripcion
            tvEstado.text = "Estado: ${service.estado}"
        } else {
            Toast.makeText(this, "No se pudo cargar el servicio", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Acción del botón volver
        btnVolver.setOnClickListener {
            finish()
        }
    }
}

