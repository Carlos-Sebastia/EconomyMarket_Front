package com.example.sebastia_carlos_proyectodi.domain.model

import java.time.LocalDate

data class Usuario(
    val dni: String,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val fechaNacimiento: LocalDate
)