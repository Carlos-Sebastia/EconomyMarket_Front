package com.example.sebastia_carlos_proyectodi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lista", primaryKeys = ["productoId", "usuarioDni"])
data class ListaEntity(
    val productoId: Long,
    val usuarioDni: String
)
