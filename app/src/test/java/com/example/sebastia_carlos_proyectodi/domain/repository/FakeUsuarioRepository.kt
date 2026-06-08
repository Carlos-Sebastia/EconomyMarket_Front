package com.example.sebastia_carlos_proyectodi.domain.repository

import com.example.sebastia_carlos_proyectodi.domain.model.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUsuarioRepository : UsuarioRepository {

    private val usuarioLogueado = MutableStateFlow<Usuario?>(null)

    var loginDeberiaSerCorrecto = true
    var mascotaDeberiaSerCorrecta = true
    var cambioContrasenaDeberiaSerCorrecto = true
    var creacionUsuarioDeberiaSerCorrecto = true

    override suspend fun validarUsuario(dni : String, contrasena : String) : Boolean {
        return loginDeberiaSerCorrecto
    }
    override suspend fun validarMascota(dni: String, mascota: String) : Boolean {
        return mascotaDeberiaSerCorrecta
    }
    override suspend fun cambiarContrasena(dni: String, nuevaContrasena: String): Boolean {
        return cambioContrasenaDeberiaSerCorrecto
    }
    override suspend fun crearUsuario(dni: String, nombre: String, apellidos: String, fechaNacimiento: String, email: String, mascota: String, contrasena: String): Boolean {
    return creacionUsuarioDeberiaSerCorrecto
    }
    override fun obtenerUsuarioLogueado(): Flow<Usuario?> = usuarioLogueado
    override suspend fun cerrarSesion() {
        usuarioLogueado.value = null
    }

}