package com.example.sebastia_carlos_proyectodi.ui.tiendas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sebastia_carlos_proyectodi.MyApplication
import com.example.sebastia_carlos_proyectodi.domain.model.Tienda
import com.example.sebastia_carlos_proyectodi.domain.repository.TiendaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TiendasViewModel(private val repository: TiendaRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(TiendasUiState())
    val uiState: StateFlow<TiendasUiState> = _uiState.asStateFlow()

    init {
        cargarTiendas()
    }

    fun cargarTiendas() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val lista = repository.obtenerTiendas()
                _uiState.update { it.copy(tiendas = lista, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                val repository = application.container.tiendaRepository
                TiendasViewModel(repository = repository)
            }
        }
    }
}

data class TiendasUiState(
    val tiendas : List<Tienda> = emptyList(),
    val isLoading : Boolean = true,
    val error: String? = null
)