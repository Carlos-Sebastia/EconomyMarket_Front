package com.example.sebastia_carlos_proyectodi.domain.repository

interface UsuarioRepository {
    suspend fun validarUsuario(dni: String, contrasena: String): Boolean
    suspend fun validarMascota(dni: String, mascota: String): Boolean
}