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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class ProductosViewModel(private val repository : ProductoRepository) : ViewModel() {

    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ProductosUiState> = repository.getProductosStream()
        .combine(_categoriaSeleccionada) { productos, categoria ->
            val productosFiltrados = if (categoria == null) {
                productos
            } else {
                productos.filter { it.categoria == categoria }
            }
            ProductosUiState(
                productos = productosFiltrados,
                categoriaSeleccionada = categoria,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProductosUiState(isLoading = true)
        )

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            try {
                repository.refreshProductos()
            } catch (e: IOException) {
                println("Error de conexión: ${e.message}")
            } catch (e: Exception) {
                println("Error inesperado: ${e.message}")
            }
        }
    }

    fun actualizarCategoria(nuevaCategoria: String?) {
        _categoriaSeleccionada.value = nuevaCategoria
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