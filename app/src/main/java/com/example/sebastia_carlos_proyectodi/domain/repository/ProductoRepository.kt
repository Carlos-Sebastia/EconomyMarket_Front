package com.example.sebastia_carlos_proyectodi.domain.repository

import com.example.sebastia_carlos_proyectodi.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    fun getProductosStream(): Flow<List<Producto>>
    suspend fun refreshProductos()
    suspend fun obtenerProductos(): List<Producto>
    fun getIdsEnLista(): Flow<List<Long>>

    suspend fun insertarEnLista(productoId: Long)
    suspend fun eliminarDeLista(productoId: Long)
    suspend fun vaciarLista()
}