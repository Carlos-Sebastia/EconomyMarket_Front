package com.example.sebastia_carlos_proyectodi.domain.repository

import com.example.sebastia_carlos_proyectodi.data.local.ListaEntity
import com.example.sebastia_carlos_proyectodi.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    fun getTodasLasEntradas(): Flow<List<ListaEntity>>
    fun getProductosStream(): Flow<List<Producto>>
    suspend fun refreshProductos()
    suspend fun obtenerProductos(): List<Producto>
    fun getIdsEnLista(usuarioDni: String): Flow<List<Long>>

    suspend fun insertarEnLista(productoId: Long, usuarioDni: String)
    suspend fun eliminarDeLista(productoId: Long, usuarioDni: String)
    suspend fun vaciarLista(usuarioDni: String)
}