package com.example.sebastia_carlos_proyectodi.data.repository

import android.util.Log
import com.example.sebastia_carlos_proyectodi.data.local.UsuarioDao
import com.example.sebastia_carlos_proyectodi.data.remote.UsuarioApiService
import com.example.sebastia_carlos_proyectodi.data.toEntity
import com.example.sebastia_carlos_proyectodi.domain.repository.UsuarioRepository
import kotlinx.coroutines.Dispatchers
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
                // 2. Si es válido, guardamos los datos públicos en la DB local
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
}