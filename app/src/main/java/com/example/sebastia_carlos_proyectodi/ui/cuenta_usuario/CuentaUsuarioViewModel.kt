package com.example.sebastia_carlos_proyectodi.ui.cuenta_usuario

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sebastia_carlos_proyectodi.MyApplication
import com.example.sebastia_carlos_proyectodi.domain.model.Usuario
import com.example.sebastia_carlos_proyectodi.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class CuentaUsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    val usuarioState: StateFlow<Usuario?> = repository.obtenerUsuarioLogueado()
        .stateIn(
            scope = viewModelScope,
            //el flujo trabaja mientras alguien visualiza la pantalla de cuenta de ususario
            started = SharingStarted.WhileSubscribed(5_000),// Mantiene los datos vivos 5s, aunque cambie el estado de la pantalla.
            initialValue = null
        )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                val repository = application.container.usuarioRepository
                CuentaUsuarioViewModel(repository = repository)
            }
        }
    }
}
