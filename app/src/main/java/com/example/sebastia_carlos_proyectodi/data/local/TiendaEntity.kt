package com.example.sebastia_carlos_proyectodi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tiendas")
data class TiendaEntity(
    @PrimaryKey
    val id : Int,
    val ciudad : String,
    val direccion : String,
    val imagenUrl : String
)