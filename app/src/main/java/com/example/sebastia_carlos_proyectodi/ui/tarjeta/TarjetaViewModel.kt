package com.example.sebastia_carlos_proyectodi.ui.tarjeta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sebastia_carlos_proyectodi.MyApplication
import com.example.sebastia_carlos_proyectodi.data.local.PreferenciasEntity
import com.example.sebastia_carlos_proyectodi.domain.model.Notificacion
import com.example.sebastia_carlos_proyectodi.domain.model.Usuario
import com.example.sebastia_carlos_proyectodi.domain.repository.NotificacionRepository
import com.example.sebastia_carlos_proyectodi.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TarjetaViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val notificacionRepository: NotificacionRepository

): ViewModel() {

    //Obtener flujo del usuario logueado
    val usuarioState: StateFlow<Usuario?> = usuarioRepository.obtenerUsuarioLogueado()
        .stateIn(
            scope = viewModelScope,
            //El flujo trabaja mientras se visualiza la pantalla de cuenta de ususario
            //Mantiene los datos vivos 5s, aunque cambie el estado de la pantalla.
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    //Combinamos el usuario con la tabla de preferencias
    val uiState: StateFlow<TarjetaUiState> = combine(
        usuarioState,
        usuarioRepository.getTodasLasPreferencias()
    ) { usuario, preferencias ->

        //Buscar preferencias por DNI del usuario logueado
        val preferenciasActuales = usuario?.let { usuario ->
            preferencias.find { it.usuarioDni == usuario.dni }
        }

        TarjetaUiState(
            ticketDigital = preferenciasActuales?.ticketDigital ?: false,
            economyPay = preferenciasActuales?.economyPay ?: false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TarjetaUiState()
    )

    fun onTicketDigitalChanged(nuevoValor: Boolean) {
        viewModelScope.launch {
            val usuario = usuarioRepository.obtenerUsuarioLogueado().first()
            usuario?.let { usuario ->
                //Guardar preferencias asociadas al DNI en base de datos
                usuarioRepository.guardarPreferencias(
                    PreferenciasEntity(
                        usuarioDni = usuario.dni,
                        ticketDigital = nuevoValor,
                        economyPay = uiState.value.economyPay
                    )
                )
                enviarNotificacionConfiguracion(
                    "Ticket Digital",
                    if (nuevoValor) "activado" else "desactivado"
                )
            }
        }
    }

    fun onEconomyPay(nuevoValor: Boolean) {
        viewModelScope.launch {
            val usuario = usuarioRepository.obtenerUsuarioLogueado().first()
            usuario?.let { usuario ->
                //Guardar preferencias asociadas al DNI en base de datos
                usuarioRepository.guardarPreferencias(
                    PreferenciasEntity(
                        usuarioDni = usuario.dni,
                        ticketDigital = uiState.value.ticketDigital,
                        economyPay = nuevoValor
                    )
                )
                enviarNotificacionConfiguracion(
                    "Economy Pay",
                    if (nuevoValor) "activado" else "desactivado"
                )
            }
        }
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