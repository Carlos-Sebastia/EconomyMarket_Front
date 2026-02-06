package com.example.sebastia_carlos_proyectodi.data.repository

import com.example.sebastia_carlos_proyectodi.data.local.TiendaDao
import com.example.sebastia_carlos_proyectodi.data.toDomain
import com.example.sebastia_carlos_proyectodi.data.toEntity
import com.example.sebastia_carlos_proyectodi.data.remote.TiendaApiService
import com.example.sebastia_carlos_proyectodi.domain.model.Tienda
import com.example.sebastia_carlos_proyectodi.domain.repository.TiendaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TiendaRepositoryImpl(
    private val api: TiendaApiService,
    private val tiendaDao: TiendaDao
) : TiendaRepository {

    override fun getTiendasStream(): Flow<List<Tienda>> {
        return tiendaDao.getAllTiendas().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshTiendas() = withContext(Dispatchers.IO) {
        try {
            val respuesta = api.getTiendas()
            val entidades = respuesta.map { it.toEntity() }

            tiendaDao.deleteAllTiendas()
            tiendaDao.insertTiendas(entidades)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun obtenerTiendas(): List<Tienda> = withContext(Dispatchers.IO) {
        refreshTiendas()
        tiendaDao.getAllTiendasOnce().map { it.toDomain() }
    }
}