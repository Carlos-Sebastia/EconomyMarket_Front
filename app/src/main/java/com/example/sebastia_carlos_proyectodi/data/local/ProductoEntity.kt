package com.example.sebastia_carlos_proyectodi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val precio: String,
    val imagenUrl: String,
    val catergoria: String
)