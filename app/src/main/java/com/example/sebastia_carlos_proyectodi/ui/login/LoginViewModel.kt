package com.example.sebastia_carlos_proyectodi.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sebastia_carlos_proyectodi.MyApplication
import com.example.sebastia_carlos_proyectodi.domain.repository.UsuarioRepository
import com.example.sebastia_carlos_proyectodi.ui.productos.ProductosViewModel
import kotlinx.coroutines.flow.combine

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {

    //Función para validar usuario
    suspend fun validarUsuario(dni: String, contrasena: String) : Boolean {
        Log.e("LoginViewModel", "Validando usuario: $dni, $contrasena")
        return repository.validarUsuario(dni, contrasena)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                val repository = application.container.usuarioRepository
                LoginViewModel(repository = repository)
            }
        }
    }
}