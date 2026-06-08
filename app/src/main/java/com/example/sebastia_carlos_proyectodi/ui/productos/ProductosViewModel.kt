package com.example.sebastia_carlos_proyectodi.ui.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sebastia_carlos_proyectodi.MyApplication
import com.example.sebastia_carlos_proyectodi.domain.model.Producto
import com.example.sebastia_carlos_proyectodi.domain.repository.ProductoRepository
import com.example.sebastia_carlos_proyectodi.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class ProductosViewModel(private val repository : ProductoRepository, private val usuarioRepository: UsuarioRepository) : ViewModel() {

    //Guardar estado de categoría seleccionada
    private val _categoriaSeleccionada = MutableStateFlow<String?>(null)
    //Guardar estado de items seleccionados
    private val _idsProductosListaCogidos = MutableStateFlow<Set<Long>>(emptySet())

    //Combina todos los flujos de datos
    val uiState: StateFlow<ProductosUiState> = combine(
        repository.getProductosStream(), //Productos de Room
        repository.getTodasLasEntradas(), //Tabla de la lista de todos los usuarios
        usuarioRepository.obtenerUsuarioLogueado(), //Usuario logueado
        _categoriaSeleccionada, //Categoría filtrada
        _idsProductosListaCogidos //Productos de la lista tachados
    ) { productos, todasLasEntradas, usarioLogueado, categoria, cogidos ->

        val dniActual = usarioLogueado?.dni ?: ""
        //Filtrar las entradas de la lista por el dni del usuario logueado
        val idsEnLista = todasLasEntradas.filter {it.usuarioDni == dniActual }.map { it.productoId }

        val productosFiltrados = if (categoria == null) {
            productos
        } else {
            productos.filter { it.categoria == categoria }
        }

        //Se devuelve el estado actualizado
        ProductosUiState(
            productos = productosFiltrados,
            todosLosProductos = productos,
            idsEnLista = idsEnLista,
            idsProductosListaCogidos = cogidos,
            categoriaSeleccionada = categoria,
            isLoading = false
        )
    }
        //Crea un estado de la pantalla. Mantiene los datos vivos durante 5 segundos
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProductosUiState(isLoading = true)
        )

    init {
        cargarProductos()
    }

    //Gestiona los productos cogidos de la lista de la compra
    fun actualizarProductoCogido(productoId: Long) {
        _idsProductosListaCogidos.update { actuales ->
            if (productoId in actuales) actuales - productoId else actuales + productoId
        }
    }

    //Añade/elimina productos de la lista asociando las acciones al usuario
    fun updateListaItems(producto: Producto, isInList: Boolean) {
        viewModelScope.launch {
            //Uso de "first" para obtener el primer elemento del flow
            val usuario = usuarioRepository.obtenerUsuarioLogueado().first()
            usuario?.dni?.let { dni ->
                if (isInList) {
                    repository.eliminarDeLista(producto.id, dni)
                } else {
                    repository.insertarEnLista(producto.id, dni)
                }
            }
        }
    }

    //Vacía la lista del usuario logueado
    fun vaciarLista() {
        viewModelScope.launch {
            val usuario = usuarioRepository.obtenerUsuarioLogueado().first()
            usuario?.dni?.let { dni ->
                repository.vaciarLista(dni)
                _idsProductosListaCogidos.value = emptySet()
            }
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

    //Inyección de dependencias para creación del ViewModel
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                ProductosViewModel(
                    repository = application.container.productoRepository,
                    usuarioRepository = application.container.usuarioRepository
                )
            }
        }
    }
}

//Elementos que necesita la UI
data class ProductosUiState (
    val productos: List<Producto> = emptyList(),
    val todosLosProductos: List<Producto> = emptyList(),
    val idsEnLista: List<Long> = emptyList(),
    val idsProductosListaCogidos: Set<Long> = emptySet(),
    val categoriaSeleccionada: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)