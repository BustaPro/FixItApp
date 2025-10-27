package com.example.fixitapp.model

data class Service(
    val id: Int = 0,
    val nombreCliente: String,
    val tipoServicio: String,
    val fecha: String,
    val descripcion: String,
    val estado: String
)
