package com.example.sebastia_carlos_proyectodi.ui.tarjeta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sebastia_carlos_proyectodi.ui.HomeViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TarjetaViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(TarjetaUiState())
    val uiState: StateFlow<TarjetaUiState> = _uiState.asStateFlow()

    fun onTicketDigitalChanged(nuevoValor: Boolean) {
        _uiState.update { it.copy(ticketDigital = nuevoValor) }
    }

    fun onEconomyPay(nuevoValor: Boolean) {
        _uiState.update { it.copy(economyPay = nuevoValor) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { TarjetaViewModel() }
        }
    }
}