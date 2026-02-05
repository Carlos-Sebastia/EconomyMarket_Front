package com.example.sebastia_carlos_proyectodi.data.repository

import com.example.sebastia_carlos_proyectodi.data.remote.TiendaApiService
import com.example.sebastia_carlos_proyectodi.domain.model.Tienda
import com.example.sebastia_carlos_proyectodi.domain.repository.TiendaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class TiendaRepositoryImpl(private val api: TiendaApiService) : TiendaRepository {
    override suspend fun obtenerTiendas(): List<Tienda> = withContext(Dispatchers.IO) {
        try {
            val respuesta = api.getTiendas()

            // Mapeo de DTO a Tienda
            respuesta.map { dto ->
                Tienda(
                    id = dto.id.toInt(),
                    ciudad = dto.ciudad,
                    direccion = dto.direccion,
                    imagenUrl = dto.imagen
                )
            }
        } catch (e: Exception) {
            throw Exception("Error al cargar tiendas: ${e.localizedMessage}")
        }
    }
}