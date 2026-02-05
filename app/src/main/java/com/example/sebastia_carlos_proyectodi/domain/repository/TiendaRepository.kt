package com.example.sebastia_carlos_proyectodi.domain.repository

import com.example.sebastia_carlos_proyectodi.domain.model.Tienda

interface TiendaRepository {
    suspend fun obtenerTiendas(): List<Tienda>
}