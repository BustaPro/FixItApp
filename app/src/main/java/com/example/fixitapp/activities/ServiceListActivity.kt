package com.example.fixitapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixitapp.R
import com.example.fixitapp.data.DatabaseHelper
import com.example.fixitapp.model.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServiceListActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ServiceAdapter
    private val services = mutableListOf<Service>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_list)

        db = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewServices)

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnAddService = findViewById<RelativeLayout>(R.id.btnAddService)

        adapter = ServiceAdapter(
            services,
            onDeleteClick = { service ->
                lifecycleScope.launch(Dispatchers.IO) {
                    db.deleteService(service.id)
                    withContext(Dispatchers.Main) {
                        services.remove(service)
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this@ServiceListActivity, "Servicio eliminado", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onItemClick = { service ->
                val intent = Intent(this, ServiceDetailsActivity::class.java)
                intent.putExtra("service_id", service.id)
                startActivity(intent)
            }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        btnAddService.setOnClickListener {
            startActivity(Intent(this, NewServiceActivity::class.java))
        }

        btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        loadServicesAsync()
    }

    override fun onResume() {
        super.onResume()
        loadServicesAsync()
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }


    private fun loadServicesAsync() {
        lifecycleScope.launch(Dispatchers.IO) {
            val fresh = db.getAllServices()
            withContext(Dispatchers.Main) {
                services.clear()
                services.addAll(fresh)
                adapter.notifyDataSetChanged()
            }
        }
    }
}
