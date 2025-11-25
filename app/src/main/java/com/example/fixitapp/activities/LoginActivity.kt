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

// IMPORTS NECESARIOS PARA RETROFIT
import com.example.fixitapp.network.RetrofitClient
import com.example.fixitapp.network.ApiService
import com.example.fixitapp.model.LoginRequest
import com.example.fixitapp.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var api: ApiService   // <-- ESTA ES LA REFERENCIA QUE TE FALTABA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)

        // Inicializamos Retrofit correctamente
        api = RetrofitClient.apiService

        val rootLogin = findViewById<android.widget.LinearLayout>(R.id.rootLogin)

        // --------------------------
        // ANIMACIÓN SUAVE AL ENTRAR
        // --------------------------
        val slide = TranslateAnimation(0f, 0f, 200f, 0f).apply {
            duration = 600
            interpolator = DecelerateInterpolator()
        }

        val fade = AlphaAnimation(0f, 1f).apply {
            duration = 600
        }

        val animSet = AnimationSet(true)
        animSet.addAnimation(slide)
        animSet.addAnimation(fade)

        rootLogin.startAnimation(animSet)

        // --------------------------
        // LÓGICA ORIGINAL DEL LOGIN
        // --------------------------
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnGoToRegister = findViewById<Button>(R.id.btnGoToRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --------------------------
            // NUEVA LÓGICA: CONSUMIR BACKEND
            // --------------------------
            val request = LoginRequest(email, password)

            api.loginUser(request).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        // Login OK desde backend
                        Toast.makeText(
                            this@LoginActivity,
                            "Inicio de sesión exitoso",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@LoginActivity, ServiceListActivity::class.java)
                        intent.putExtra("usuario", email)
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Correo o contraseña incorrectos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error de conexión con el servidor",
                        Toast.LENGTH_SHORT
                    ).show()
                    t.printStackTrace()
                }
            })
        }

        btnGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
