package com.example.fixitapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixitapp.R
import com.example.fixitapp.data.DatabaseHelper
import com.example.fixitapp.model.Service

class ServiceListActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ServiceAdapter
    private lateinit var services: MutableList<Service>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_list)

        // Inicializar DB y RecyclerView
        db = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewServices)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // Cargar servicios desde la base de datos
        services = db.getAllServices().toMutableList()



        // Configurar adapter con opción de eliminar
        adapter = ServiceAdapter(services) { service ->
            db.deleteService(service.id)
            services.remove(service)
            adapter.notifyDataSetChanged()
            Toast.makeText(this, "Servicio eliminado", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val btnAddService = findViewById<Button>(R.id.btnAddService)
        btnAddService.setOnClickListener {
            val intent = Intent(this, NewServiceActivity::class.java)
            startActivity(intent)
        }


        // Botón para cerrar sesión
        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // recargar datos desde la DB y notificar al adapter
        val fresh = db.getAllServices()
        services.clear()
        services.addAll(fresh)
        adapter.notifyDataSetChanged()
    }

}
