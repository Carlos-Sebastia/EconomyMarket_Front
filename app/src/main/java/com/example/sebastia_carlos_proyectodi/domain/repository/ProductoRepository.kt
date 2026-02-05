package com.example.sebastia_carlos_proyectodi.domain.repository

import com.example.sebastia_carlos_proyectodi.domain.model.Producto

interface ProductoRepository {
    suspend fun obtenerProductos(): List<Producto>
}