package com.example.fixitapp.data

import com.example.fixitapp.model.Usuario

object UsuarioData {
    val listaUsuarios = mutableListOf<Usuario>()

    fun agregarUsuario(usuario: Usuario) {
        listaUsuarios.add(usuario)
    }

    fun obtenerUsuarioPorEmail(email: String): Usuario? {
        return listaUsuarios.find { it.email == email }
    }
}