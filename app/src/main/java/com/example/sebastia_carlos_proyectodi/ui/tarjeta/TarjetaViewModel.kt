package com.example.sebastia_carlos_proyectodi.ui.tarjeta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sebastia_carlos_proyectodi.MyApplication
import com.example.sebastia_carlos_proyectodi.domain.model.Notificacion
import com.example.sebastia_carlos_proyectodi.domain.model.Usuario
import com.example.sebastia_carlos_proyectodi.domain.repository.NotificacionRepository
import com.example.sebastia_carlos_proyectodi.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TarjetaViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val notificacionRepository: NotificacionRepository

): ViewModel() {
    private val _uiState = MutableStateFlow(TarjetaUiState())
    val uiState: StateFlow<TarjetaUiState> = _uiState.asStateFlow()

    val usuarioState: StateFlow<Usuario?> = usuarioRepository.obtenerUsuarioLogueado()
        .stateIn(
            scope = viewModelScope,
            //El flujo trabaja mientras alguien visualiza la pantalla de cuenta de ususario
            //Mantiene los datos vivos 5s, aunque cambie el estado de la pantalla.
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun onTicketDigitalChanged(nuevoValor: Boolean) {
        _uiState.update { it.copy(ticketDigital = nuevoValor) }
        enviarNotificacionConfiguracion(
            "Ticket Digital",
            if (nuevoValor) "activado" else "desactivado"
        )
    }

    fun onEconomyPay(nuevoValor: Boolean) {
        _uiState.update { it.copy(economyPay = nuevoValor) }
        enviarNotificacionConfiguracion(
            "Economy Pay",
            if (nuevoValor) "activado" else "desactivado"
        )
    }

    private fun enviarNotificacionConfiguracion(ajuste: String, estado: String) {
        val usuario = usuarioState.value ?: return
        val formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val fechaFormateada = LocalDateTime.now().format(formatoFecha)
        viewModelScope.launch {
            notificacionRepository.enviarNotificacion(
                Notificacion(
                    id = 0,
                    titulo = "Seguridad",
                    mensaje = "Has $estado el ajuste: $ajuste",
                    fecha = fechaFormateada,
                    dniUsuario = usuario.dni
                )
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                TarjetaViewModel(
                    usuarioRepository = application.container.usuarioRepository,
                    notificacionRepository = application.container.notificacionRepository
                )
            }
        }
    }
}