package com.example.fixitapp.network

import com.example.fixitapp.model.ExternalService
import retrofit2.Call
import retrofit2.http.GET

interface ExternalApiService {

    @GET("posts")
    fun getServiciosExternos(): Call<List<ExternalService>>
}
