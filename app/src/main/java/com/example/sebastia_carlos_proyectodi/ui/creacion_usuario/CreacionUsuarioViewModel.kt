package com.example.sebastia_carlos_proyectodi.ui.creacion_usuario

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sebastia_carlos_proyectodi.MyApplication
import com.example.sebastia_carlos_proyectodi.domain.repository.UsuarioRepository

class CreacionUsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {
    suspend fun crearUsuario(dni: String, nombre: String, apellidos: String, fechaNacimiento: String, email: String, mascota: String, contrasena: String): Boolean {
        Log.e("CreacionUsuarioViewModel", "Creando usuario: $dni, $nombre, $apellidos, $fechaNacimiento, $email, $mascota, $contrasena")
        return repository.crearUsuario(dni, nombre, apellidos, fechaNacimiento, email, mascota, contrasena)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                val repository = application.container.usuarioRepository
                CreacionUsuarioViewModel(repository = repository)
            }
        }
    }
}
