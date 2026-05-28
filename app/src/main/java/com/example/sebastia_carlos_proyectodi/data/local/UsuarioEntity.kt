package com.example.sebastia_carlos_proyectodi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey val dni: String,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val fechaNacimiento: String
)