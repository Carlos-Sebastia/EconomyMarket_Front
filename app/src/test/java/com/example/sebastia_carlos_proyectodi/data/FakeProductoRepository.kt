package com.example.sebastia_carlos_proyectodi.data

import com.example.sebastia_carlos_proyectodi.domain.model.Producto
import com.example.sebastia_carlos_proyectodi.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeProductoRepository : ProductoRepository {

    // Simulamos una base de datos en memoria
    private val productosStream = MutableStateFlow<List<Producto>>(emptyList())
    private val idsEnListaStream = MutableStateFlow<List<Long>>(emptyList())

    override fun getProductosStream(): Flow<List<Producto>> = productosStream

    override fun getIdsEnLista(): Flow<List<Long>> = idsEnListaStream

    fun emitirProductos(nuevaLista: List<Producto>) {
        productosStream.value = nuevaLista
    }

    override suspend fun refreshProductos() {
    }

    override suspend fun obtenerProductos(): List<Producto> = productosStream.value

    override suspend fun insertarEnLista(productoId: Long) {
        idsEnListaStream.value = idsEnListaStream.value + productoId
    }

    override suspend fun eliminarDeLista(productoId: Long) {
        idsEnListaStream.value = idsEnListaStream.value - productoId
    }

    override suspend fun vaciarLista() {
        idsEnListaStream.value = emptyList()
    }
}