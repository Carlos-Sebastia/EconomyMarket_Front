package com.example.sebastia_carlos_proyectodi.domain.repository

interface UsuarioRepository {
    suspend fun validarUsuario(dni: String, contrasena: String): Boolean
}