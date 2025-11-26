package com.example.fixitapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fixitapp.R
import com.example.fixitapp.data.DatabaseHelper
import android.util.Log
import com.example.fixitapp.network.RetrofitClient
import com.example.fixitapp.network.ApiService
import com.example.fixitapp.model.LoginRequest
import com.example.fixitapp.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody


class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoToRegister = findViewById<Button>(R.id.btnGoToRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 👉 JSON para API (OkHttp)
            val json = """
                {
                    "email": "$email",
                    "password": "$password"
                }
            """.trimIndent()

            val client = okhttp3.OkHttpClient()
            val mediaType = okhttp3.MediaType.parse("application/json; charset=utf-8")
            val body = okhttp3.RequestBody.create(mediaType, json)

            val request = okhttp3.Request.Builder()
                .url("https://backend-login-a2iz.onrender.com/login")
                .post(body)
                .build()

            Thread {
                try {
                    val response = client.newCall(request).execute()
                    val responseBody = response.body()?.string()
                    val loginExitosoApi = responseBody?.contains("\"success\":true") == true

                    // Revisamos login local también
                    val loginExitosoLocal = dbHelper.validateUser(email, password)

                    runOnUiThread {
                        if (loginExitosoApi || loginExitosoLocal) {
                            // Guardar sesión
                            val sharedPrefs = getSharedPreferences("FixItSession", MODE_PRIVATE)
                            sharedPrefs.edit().putString("USER_EMAIL", email).apply()

                            Toast.makeText(
                                this,
                                "Inicio de sesión exitoso",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Navegar a servicios
                            startActivity(Intent(this, ServiceListActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Correo o contraseña incorrectos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Error de conexión: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }.start()
        }

        btnGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
