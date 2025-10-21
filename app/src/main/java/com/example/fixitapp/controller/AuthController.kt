package com.example.fixitapp.controller

import com.example.fixitapp.data.UsuarioData
import com.example.fixitapp.model.Usuario

object AuthController {

    fun registrarUsuario(nombre: String, email: String, password: String): Boolean {
        if (UsuarioData.obtenerUsuarioPorEmail(email) != null) {
            return false // Ya existe un usuario con ese correo
        }
        val nuevoUsuario = Usuario(
            id = UsuarioData.listaUsuarios.size + 1,
            nombre = nombre,
            email = email,
            password = password
        )
        UsuarioData.agregarUsuario(nuevoUsuario)
        return true
    }

    fun loginUsuario(email: String, password: String): Usuario? {
        val usuario = UsuarioData.obtenerUsuarioPorEmail(email)
        return if (usuario != null && usuario.password == password) usuario else null
    }
}