package com.example.fixitapp.activities

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fixitapp.R
import com.example.fixitapp.data.DatabaseHelper
import java.util.*

class NewServiceActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_service)

        dbHelper = DatabaseHelper(this)

        val etNombre = findViewById<EditText>(R.id.etNombreCliente)
        val etTipo = findViewById<EditText>(R.id.etTipoServicio)
        val etFecha = findViewById<EditText>(R.id.etFecha)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarServicio)

        // 📅 Selector de fecha nativo
        etFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, day ->
                    val fecha = "$day/${month + 1}/$year"
                    etFecha.setText(fecha)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val tipo = etTipo.text.toString().trim()
            val fecha = etFecha.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()

            if (nombre.isEmpty() || tipo.isEmpty() || fecha.isEmpty()) {
                Toast.makeText(this, "⚠️ Todos los campos obligatorios deben completarse", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("nombreCliente", nombre)
                put("tipoServicio", tipo)
                put("fecha", fecha)
                put("descripcion", descripcion)
                put("estado", "Pendiente")
            }

            val result = db.insert("services", null, values)
            db.close()

            if (result != -1L) {
                // 💥 Vibración (recurso nativo)
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(100)

                Toast.makeText(this, "✅ Servicio guardado correctamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "❌ Error al guardar el servicio", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
