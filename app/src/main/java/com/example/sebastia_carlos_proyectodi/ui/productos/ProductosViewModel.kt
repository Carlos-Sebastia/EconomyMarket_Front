package com.example.sebastia_carlos_proyectodi.ui.productos

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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class ProductosViewModel(private val repository : ProductoRepository) : ViewModel() {

    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)

    // Nuevo estado para los productos tachados en la lista (UI State persistente)
    private val _idsProductosListaCogidos = MutableStateFlow<Set<Long>>(emptySet())

    val uiState: StateFlow<ProductosUiState> = combine(
        repository.getProductosStream(),
        repository.getIdsEnLista(),
        _categoriaSeleccionada,
        _idsProductosListaCogidos // Combinamos el cuarto flujo
    ) { productos, ids, categoria, cogidos ->
        val productosFiltrados = if (categoria == null) {
            productos
        } else {
            productos.filter { it.categoria == categoria }
        }

        ProductosUiState(
            productos = productosFiltrados,
            idsEnLista = ids,
            idsProductosListaCogidos = cogidos, // Pasamos el set de IDs marcados
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

    fun actualizarProductoCogido(productoId: Long) {
        _idsProductosListaCogidos.update { actuales ->
            if (productoId in actuales) actuales - productoId else actuales + productoId
        }
    }

    fun updateListaItems(producto: Producto, isInList: Boolean) {
        viewModelScope.launch {
            if (isInList) {
                repository.eliminarDeLista(producto.id)
            } else {
                repository.insertarEnLista(producto.id)
            }
        }
    }

    fun vaciarLista() {
        viewModelScope.launch {
            repository.vaciarLista()
            // Al vaciar la lista de la compra, limpiamos también los checks visuales
            _idsProductosListaCogidos.value = emptySet()
        }
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
    val idsEnLista: List<Long> = emptyList(),
    val idsProductosListaCogidos: Set<Long> = emptySet(),
    val categoriaSeleccionada: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)