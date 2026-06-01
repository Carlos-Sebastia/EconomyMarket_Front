package com.example.sebastia_carlos_proyectodi.domain.repository

interface UsuarioRepository {
    suspend fun validarUsuario(dni: String, contrasena: String): Boolean
    suspend fun validarMascota(dni: String, mascota: String): Boolean
    suspend fun cambiarContrasena(dni: String, nuevaContrasena: String): Boolean
    suspend fun crearUsuario(dni: String, nombre: String, apellidos: String, fechaNacimiento: String, email: String, mascota: String, contrasena: String): Boolean
}