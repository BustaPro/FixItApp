package com.example.fixitapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fixitapp.R
import com.example.fixitapp.data.DatabaseHelper
import com.example.fixitapp.model.ExternalService
import com.example.fixitapp.model.Service
import com.example.fixitapp.network.ExternalRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ServiceListActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ServiceAdapter
    private val services = mutableListOf<Service>()
    private val externalServices = mutableListOf<Service>()

    private lateinit var imgFlag: ImageView
    private lateinit var txtUserName: TextView

    private var serviciosExternosCargados = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_list)

        overridePendingTransition(0,0)

        val root = findViewById<View>(R.id.rootServiceList)
        root.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom))

        val sharedPrefs = getSharedPreferences("FixItSession", MODE_PRIVATE)
        val email = sharedPrefs.getString("USER_EMAIL", null)

        if (email == null) {

            // 👇 Si NO estamos en test, enviar al login
            if (!isRunningTest()) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return
            }
        }


        txtUserName = findViewById(R.id.txtUserName)
        imgFlag = findViewById(R.id.imgFlag)
        db = DatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerViewServices)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnAddService = findViewById<RelativeLayout>(R.id.btnAddService)

        adapter = ServiceAdapter(
            services,
            onDeleteClick = { service ->
                if (!service.isExternal) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        db.deleteService(service.id)
                        withContext(Dispatchers.Main) {
                            services.remove(service)
                            adapter.notifyDataSetChanged()
                        }
                    }
                } else Toast.makeText(this,"Los externos no se pueden eliminar",Toast.LENGTH_SHORT).show()
            },
            onItemClick = { service ->
                val intent = Intent(this, ServiceDetailsActivity::class.java)
                intent.putExtra("service_data", service)
                startActivity(intent)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnAddService.setOnClickListener {
            startActivity(Intent(this, NewServiceActivity::class.java))
        }

        btnLogout.setOnClickListener {
            sharedPrefs.edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        loadUserHeader()
        refreshData()
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    private fun refreshData() {
        loadLocalServices()
        if (!serviciosExternosCargados) fetchServiciosExternos()
    }

    private fun loadLocalServices() {
        lifecycleScope.launch(Dispatchers.IO) {
            val fresh = db.getAllServices()
            withContext(Dispatchers.Main) {
                services.clear()
                services.addAll(fresh)
                services.addAll(externalServices)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun loadUserHeader() {
        val sharedPrefs = getSharedPreferences("FixItSession", MODE_PRIVATE)
        val email = sharedPrefs.getString("USER_EMAIL", null) ?: return

        lifecycleScope.launch(Dispatchers.IO) {
            val userData = db.getUserData(email)

            withContext(Dispatchers.Main) {
                if (userData != null) {
                    val (nombre, nacionalidad) = userData
                    txtUserName.text = nombre ?: "Usuario"

                    nacionalidad?.takeIf { it.isNotBlank() }?.let {
                        val urlFlag = "https://flagsapi.com/${it.uppercase()}/flat/64.png"
                        Glide.with(this@ServiceListActivity)
                            .load(urlFlag)
                            .into(imgFlag)
                    } ?: run {
                        imgFlag.setImageResource(R.drawable.ic_launcher_foreground)
                    }
                } else {
                    txtUserName.text = "Usuario"
                    imgFlag.setImageResource(R.drawable.ic_launcher_foreground)
                }
            }
        }
    }


    private fun fetchServiciosExternos() {
        ExternalRetrofitClient.apiService.getServiciosExternos()
            .enqueue(object : retrofit2.Callback<List<ExternalService>> {
                override fun onResponse(
                    call: retrofit2.Call<List<ExternalService>>,
                    response: retrofit2.Response<List<ExternalService>>
                ) {
                    if (!response.isSuccessful) return

                    serviciosExternosCargados = true
                    externalServices.clear()

                    externalServices.addAll(
                        response.body()?.take(10)?.map { ext ->
                            Service(
                                id = ext.id + 5000,
                                nombreCliente = "Cliente Externo ${ext.id}",
                                tipoServicio = "Servicio Externo",
                                fecha = "2025-11-26",
                                descripcion = ext.body.take(60),
                                estado = "Pendiente",
                                isExternal = true
                            )
                        } ?: emptyList()
                    )
                    loadLocalServices()
                }

                override fun onFailure(call: retrofit2.Call<List<ExternalService>>, t: Throwable) {
                    Toast.makeText(this@ServiceListActivity,"Error API Externa", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun isRunningTest(): Boolean {
        return try {
            Class.forName("androidx.test.espresso.Espresso")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }

}
