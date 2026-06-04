package com.example.sebastia_carlos_proyectodi.domain.model

data class Notificacion(
    val id: Long,
    val titulo: String,
    val mensaje: String,
    val fecha: String,
    val dniUsuario: String
)
