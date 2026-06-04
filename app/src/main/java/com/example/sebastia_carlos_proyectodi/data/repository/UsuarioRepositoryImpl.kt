package com.example.sebastia_carlos_proyectodi.data.repository

import android.util.Log
import com.example.sebastia_carlos_proyectodi.data.local.UsuarioDao
import com.example.sebastia_carlos_proyectodi.data.local.UsuarioEntity
import com.example.sebastia_carlos_proyectodi.data.remote.UsuarioApiService
import com.example.sebastia_carlos_proyectodi.data.toDomain
import com.example.sebastia_carlos_proyectodi.data.toEntity
import com.example.sebastia_carlos_proyectodi.domain.model.Usuario
import com.example.sebastia_carlos_proyectodi.domain.repository.UsuarioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UsuarioRepositoryImpl(
    private val api: UsuarioApiService,
    private val usuarioDao: UsuarioDao
) : UsuarioRepository {
    override suspend fun validarUsuario(dni: String, contrasena: String): Boolean = withContext(Dispatchers.IO) {
        try {
            // 1. La contraseña se valida contra la API (Network), NO contra el DAO
            val usuarioDto = api.login(dni, contrasena)
            Log.e("Login", "Usuario obtenido: $usuarioDto")

            if (usuarioDto != null) {
                // 2. Si es válido, guardamos los datos públicos en la DB local
                usuarioDao.borrarUsuarios()
                usuarioDao.insertarUsuario(usuarioDto.toEntity())
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("LoginError", "Error al validar usuario y contraseña: ${e.message}")
            e.printStackTrace() // Esto te dirá en el Logcat exactamente qué falla
            false
        }
    }

    override suspend fun validarMascota(dni: String, mascota: String): Boolean = withContext(Dispatchers.IO) {
        try {
            // 1. La mascota se valida contra la API (Network), NO contra el DAO
            val usuarioDto = api.validarMascota(dni, mascota)
            Log.e("Validación mascota", "DNI y mascota obtenidos: $dni, $usuarioDto")

            if (usuarioDto != null) {
                // 2. Si es válido, borramos la información de usuarios anteriores y guardamos los datos públicos en la DB local
                usuarioDao.borrarUsuarios()
                usuarioDao.insertarUsuario(usuarioDto.toEntity())
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("LoginError", "Error al validar usuario y mascota: ${e.message}")
            e.printStackTrace() // Esto te dirá en el Logcat exactamente qué falla
            false
        }
    }

    override suspend fun cambiarContrasena(dni: String, nuevaContrasena: String): Boolean = withContext(Dispatchers.IO) {
        try {
            Log.e("Cambio contraseña", "DNI y contraseña obtenidos: $dni, $nuevaContrasena")
            api.cambiarContrasena(dni, nuevaContrasena)
        } catch (e: Exception) {
            Log.e("LoginError", "Error al cambiar contraseña: ${e.message}")
            false
        }
    }

    override suspend fun crearUsuario(dni: String, nombre: String, apellidos: String, fechaNacimiento: String, email: String, mascota: String, contrasena: String): Boolean
    = withContext(Dispatchers.IO) {
        try {
            Log.e("Creacion usuario", "Creando usuario: $dni, $nombre, $apellidos, $fechaNacimiento, $email, $mascota, $contrasena")
            val exito = api.crearUsuario(dni, nombre, apellidos, fechaNacimiento, email, mascota, contrasena)
            if (exito) {
                usuarioDao.borrarUsuarios()
                usuarioDao.insertarUsuario(UsuarioEntity(dni, nombre, apellidos, email, fechaNacimiento))
                true
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e("LoginError", "Error al crear usuario: ${e.message}")
            false
        }
    }

    override fun obtenerUsuarioLogueado(): Flow<Usuario?> {
        return usuarioDao.obtenerUsuarioLogueado().map { it?.toDomain() }
    }

    override suspend fun cerrarSesion() {
        usuarioDao.borrarUsuarios()
    }
}