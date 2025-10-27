package com.example.fixitapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fixitapp.R
import com.example.fixitapp.data.DBHelper
import com.example.fixitapp.model.Service
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ServiceListActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var serviceAdapter: ServiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_list)

        dbHelper = DBHelper(this)
        recyclerView = findViewById(R.id.recyclerViewServices)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val services = dbHelper.getAllServices()
        serviceAdapter = ServiceAdapter(services)
        recyclerView.adapter = serviceAdapter

        val fabAddService = findViewById<FloatingActionButton>(R.id.fabAddService)
        fabAddService.setOnClickListener {
            val intent = Intent(this, NewServiceActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        serviceAdapter.updateList(dbHelper.getAllServices())
    }
}
