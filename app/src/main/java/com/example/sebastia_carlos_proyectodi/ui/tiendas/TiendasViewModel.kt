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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class TiendasViewModel(private val repository: TiendaRepository) : ViewModel() {
    val uiState: StateFlow<TiendasUiState> = repository.getTiendasStream()
        .map { lista ->
            TiendasUiState(tiendas = lista, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TiendasUiState(isLoading = true)
        )

    init {
        cargarTiendas()
    }

    fun cargarTiendas() {
        viewModelScope.launch {
            try {
                repository.refreshTiendas()
            } catch (e: Exception) {
                println("ERROR CRÍTICO: ${e.message}")            }
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
    val isLoading : Boolean = false,
    val error: String? = null
)