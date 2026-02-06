package com.example.sebastia_carlos_proyectodi.domain.repository

import com.example.sebastia_carlos_proyectodi.domain.model.Tienda
import kotlinx.coroutines.flow.Flow

interface TiendaRepository {
    suspend fun obtenerTiendas(): List<Tienda>

    fun getTiendasStream(): Flow<List<Tienda>>
    suspend fun refreshTiendas()
}