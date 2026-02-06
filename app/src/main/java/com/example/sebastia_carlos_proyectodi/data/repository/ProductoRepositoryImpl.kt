package com.example.sebastia_carlos_proyectodi.data.repository

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

class ProductoRepositoryImpl(
    private val api: ProductoApiService,
    private val productoDao: ProductoDao
) : ProductoRepository {

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

    override suspend fun obtenerProductos(): List<Producto> {
        refreshProductos()
        return emptyList()
    }
}