package com.example.sebastia_carlos_proyectodi.ui.productos

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sebastia_carlos_proyectodi.MyApplication
import com.example.sebastia_carlos_proyectodi.domain.model.Producto
import com.example.sebastia_carlos_proyectodi.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductosViewModel(private val repository : ProductoRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductosUiState())
    val uiState: StateFlow<ProductosUiState> = _uiState.asStateFlow()

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val lista = repository.obtenerProductos()
                _uiState.update { it.copy(productos = lista, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun actualizarCategoria(nuevaCategoria: String?) {
        _uiState.update { it.copy(categoriaSeleccionada = nuevaCategoria) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                val repository = application.container.productoRepository
                ProductosViewModel(repository = repository)
            }
        }
    }
 }



data class ProductosUiState (
    val productos: List<Producto> = emptyList(),
    val categoriaSeleccionada: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)