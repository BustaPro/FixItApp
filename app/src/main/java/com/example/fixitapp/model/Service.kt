package com.example.fixitapp.model

import java.io.Serializable

data class Service(
    val id: Int,
    val nombreCliente: String,
    val tipoServicio: String,
    val fecha: String,
    val descripcion: String,
    val estado: String,
    val isExternal: Boolean = false
) : Serializable
