package com.example.sebastia_carlos_proyectodi.data

import com.example.sebastia_carlos_proyectodi.data.local.ProductoEntity
import com.example.sebastia_carlos_proyectodi.data.remote.ProductoDto
import com.example.sebastia_carlos_proyectodi.domain.model.Producto


fun ProductoDto.toEntity(): ProductoEntity = ProductoEntity (
    id = this.id,
    nombre = this.nombre,
    precio = this.precio,
    imagen = this.imagen ?: "placeholder_default",
    categoria = this.categoria
)

fun ProductoEntity.toDomain(): Producto = Producto(
    id = this.id,
    nombre = this.nombre,
    precio = this.precio,
    imagen = this.imagen,
    categoria = this.categoria
)