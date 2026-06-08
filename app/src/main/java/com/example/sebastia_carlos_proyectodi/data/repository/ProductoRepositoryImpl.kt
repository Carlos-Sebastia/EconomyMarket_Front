package com.example.sebastia_carlos_proyectodi.data.repository

import com.example.sebastia_carlos_proyectodi.data.local.ListaDao
import com.example.sebastia_carlos_proyectodi.data.local.ListaEntity
import com.example.sebastia_carlos_proyectodi.data.local.ProductoDao
import com.example.sebastia_carlos_proyectodi.data.remote.ProductoApiService
import com.example.sebastia_carlos_proyectodi.data.toDomain
import com.example.sebastia_carlos_proyectodi.data.toEntity
import com.example.sebastia_carlos_proyectodi.domain.model.Producto
import com.example.sebastia_carlos_proyectodi.domain.repository.ProductoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.collections.map

class ProductoRepositoryImpl(
    private val api: ProductoApiService,
    private val productoDao: ProductoDao,
    private val listaDao: ListaDao
) : ProductoRepository {

    override fun getTodasLasEntradas(): Flow<List<ListaEntity>> {
        return listaDao.getTodasLasEntradas()
    }

    override fun getProductosStream(): Flow<List<Producto>> {
        return productoDao.getAllProductos().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshProductos() = withContext(Dispatchers.IO) {
        try {
            val respuesta = api.getProductos()
            val entidades = respuesta.map { it.toEntity() }

            productoDao.deleteAll()
            productoDao.insertProductos(entidades)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun obtenerProductos(): List<Producto> = withContext(Dispatchers.IO) {
        refreshProductos()
        productoDao.getAllProductosOnce().map { it.toDomain() }
    }

    override fun getIdsEnLista(usuarioDni: String): Flow<List<Long>> {
        return listaDao.getIdsEnLista(usuarioDni)
    }
    override suspend fun insertarEnLista(id: Long, usuarioDni: String) {
        listaDao.insertar(ListaEntity(id, usuarioDni))
    }
    override suspend fun eliminarDeLista(id: Long, usuarioDni: String) {
        listaDao.eliminar(ListaEntity(id, usuarioDni))
    }

    override suspend fun vaciarLista(usuarioDni: String) {
        listaDao.vaciarLista(usuarioDni)
    }
}