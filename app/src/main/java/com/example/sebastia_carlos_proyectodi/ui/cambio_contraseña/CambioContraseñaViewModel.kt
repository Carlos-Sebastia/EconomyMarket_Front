package com.example.sebastia_carlos_proyectodi.ui.cambio_contraseña

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sebastia_carlos_proyectodi.MyApplication
import com.example.sebastia_carlos_proyectodi.domain.repository.UsuarioRepository

class CambioContraseñaViewModel(private val repository: UsuarioRepository) : ViewModel() {

    var dniValidado: String = ""
        private set

    //Función para validar mascota
    suspend fun validarMascota(dni: String, mascota: String) : Boolean {
        Log.e("LoginViewModel", "Validando dni y mascota: $dni, $mascota")
        val esValido = repository.validarMascota(dni, mascota)
        if (esValido) {
            dniValidado = dni
        }
        return esValido
    }

    suspend fun cambiarContrasena(nuevaContrasena: String): Boolean {
        return repository.cambiarContrasena(dniValidado, nuevaContrasena)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                val repository = application.container.usuarioRepository
                CambioContraseñaViewModel(repository = repository)
            }
        }
    }
}