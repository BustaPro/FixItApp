package com.example.fixitapp.network.model

data class CountryResponse(
    val name: CountryName,
    val flags: CountryFlags,
    val cca2: String
)

data class CountryName(
    val common: String
)

data class CountryFlags(
    val png: String
)

