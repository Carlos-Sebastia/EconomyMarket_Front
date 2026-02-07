package com.example.sebastia_carlos_proyectodi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lista")
data class ListaEntity(
    @PrimaryKey val productoId: Long
)