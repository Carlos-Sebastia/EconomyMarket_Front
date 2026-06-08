package com.example.sebastia_carlos_proyectodi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preferencias_usuario")
data class PreferenciasEntity(
    @PrimaryKey val usuarioDni: String,
    val ticketDigital: Boolean,
    val economyPay: Boolean
    )