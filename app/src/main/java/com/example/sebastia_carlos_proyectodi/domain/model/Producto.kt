package com.example.sebastia_carlos_proyectodi.domain.model


data class Producto(
    val id: Int,
    val nombreProducto : String,
    val precioProducto : String,
    val imagenUrl: String,
    val categoria : String
)