package com.example.fixitapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fixitapp.R
import com.example.fixitapp.data.DatabaseHelper
import com.example.fixitapp.model.Service

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
        val tvTecnico = findViewById<TextView>(R.id.tvTecnicoDetail)
        val imgTecnico = findViewById<ImageView>(R.id.imgTecnicoDetail)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        val service = intent.getSerializableExtra("service_data") as? Service

        if (service == null) {
            Toast.makeText(this, "Error: servicio no encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        if (service.isExternal) {
            // 🚀 Servicio de la API externa
            tvNombre.text = service.nombreCliente
            tvTipo.text = service.tipoServicio
            tvFecha.text = service.fecha
            tvDescripcion.text = service.descripcion
            tvEstado.text = "Estado: ${service.estado}"

            tvTecnico.text = "Técnico asignado: API Externa"
            imgTecnico.setImageResource(R.drawable.tecnico2) // 👈 Un ícono de API, usa cualquiera

        } else {
            // 📌 Servicio local → desde SQLite
            tvNombre.text = service.nombreCliente
            tvTipo.text = service.tipoServicio
            tvFecha.text = service.fecha
            tvDescripcion.text = service.descripcion
            tvEstado.text = "Estado: ${service.estado}"

            val prefs = getSharedPreferences("tecnicos_fixit", MODE_PRIVATE)
            var tecnicoNombre = prefs.getString("tecnico_${service.id}", null)

            if (tecnicoNombre == null) {
                tecnicoNombre = getRandomTecnico()
                prefs.edit().putString("tecnico_${service.id}", tecnicoNombre).apply()
            }

            tvTecnico.text = "Técnico asignado: $tecnicoNombre"
            imgTecnico.setImageResource(getRandomTecnicoFoto())
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun getRandomTecnico(): String {
        val nombres = listOf("Carlos Herrera", "Pedro Muñoz", "Diego Campos",
            "Matías Vidal", "Luis Vega", "Javier Torres", "Nicolás Bravo")
        return nombres.random()
    }

    private fun getRandomTecnicoFoto(): Int {
        val fotos = listOf(
            R.drawable.tecnico1,
            R.drawable.tecnico2,
            R.drawable.tecnico3
        )
        return fotos.random()
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
