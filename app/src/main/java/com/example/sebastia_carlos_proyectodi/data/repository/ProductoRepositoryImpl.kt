package com.example.sebastia_carlos_proyectodi.data.repository

import com.example.sebastia_carlos_proyectodi.data.remote.ProductoApiService
import com.example.sebastia_carlos_proyectodi.domain.model.Producto
import com.example.sebastia_carlos_proyectodi.domain.repository.ProductoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okio.IOException

class ProductoRepositoryImpl(private val api: ProductoApiService) : ProductoRepository {

    override suspend fun obtenerProductos(): List<Producto> = withContext(Dispatchers.IO) {
        try {
            val respuesta = api.getProductos()
            respuesta.map { dto ->
                Producto(
                    id = dto.id?.toInt() ?: 0,
                    nombreProducto = dto.nomP,
                    precioProducto = dto.precio ?: "0.00 €",
                    imagenUrl = dto.imagen ?: "",
                    categoria = dto.categoria ?: "Sin categoría"
                )
            }
        } catch (e: IOException) {
            throw Exception("Error de conexión: ${e.localizedMessage}")
        }
    }
}
