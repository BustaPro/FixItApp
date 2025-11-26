package com.example.fixitapp.network

import com.example.fixitapp.network.model.CountryResponse
import retrofit2.Call
import retrofit2.http.GET

interface CountryApi {
    @GET("all")
    fun getAllCountries(): Call<List<CountryResponse>>
}
