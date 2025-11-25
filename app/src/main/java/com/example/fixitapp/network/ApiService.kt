package com.example.fixitapp.network

import com.example.fixitapp.model.LoginRequest
import com.example.fixitapp.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>
}
