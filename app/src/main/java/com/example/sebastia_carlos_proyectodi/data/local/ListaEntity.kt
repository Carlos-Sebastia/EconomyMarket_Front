package com.example.sebastia_carlos_proyectodi.data.local

import androidx.room.Entity

@Entity(tableName = "lista", primaryKeys = ["productoId", "usuarioDni"])
data class ListaEntity(
    val productoId: Long,
    val usuarioDni: String
)
