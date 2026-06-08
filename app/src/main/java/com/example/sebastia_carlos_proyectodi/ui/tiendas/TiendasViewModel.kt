package com.example.sebastia_carlos_proyectodi.ui.tiendas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sebastia_carlos_proyectodi.MyApplication
import com.example.sebastia_carlos_proyectodi.domain.model.Tienda
import com.example.sebastia_carlos_proyectodi.domain.repository.TiendaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TiendasViewModel(private val repository: TiendaRepository) : ViewModel() {
    val uiState: StateFlow<TiendasUiState> = repository.getTiendasStream()
        .map { lista ->
            //La lista de tiendas se convierte en objeto de estado
            TiendasUiState(tiendas = lista, isLoading = false)
        }
        //Crea un estado de la pantalla. Mantiene los datos vivos durante 5 segundos
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

    //Inyección de dependencias
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

//Elementos que necesita la UI
data class TiendasUiState(
    val tiendas : List<Tienda> = emptyList(),
    val isLoading : Boolean = false,
    val error: String? = null
)