package com.example.sebastia_carlos_proyectodi.ui.notificaciones

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sebastia_carlos_proyectodi.MyApplication
import com.example.sebastia_carlos_proyectodi.domain.model.Notificacion
import com.example.sebastia_carlos_proyectodi.domain.repository.NotificacionRepository
import com.example.sebastia_carlos_proyectodi.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotificacionViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val notificacionRepository: NotificacionRepository
) : ViewModel() {
    //Obtener notificaciones del usuario logueado
    val notificaciones: StateFlow<List<Notificacion>> = usuarioRepository.obtenerUsuarioLogueado()
        .flatMapLatest { usuario ->
            if (usuario != null) {
                notificacionRepository.getNotificaciones(usuario.dni)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    //Borrar notificación del menú
    fun borrarNotificacion(id: Long) {
        viewModelScope.launch {
            notificacionRepository.borrarNotificacion(id)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                NotificacionViewModel(
                    usuarioRepository = application.container.usuarioRepository,
                    notificacionRepository = application.container.notificacionRepository
                )
            }
        }
    }
}