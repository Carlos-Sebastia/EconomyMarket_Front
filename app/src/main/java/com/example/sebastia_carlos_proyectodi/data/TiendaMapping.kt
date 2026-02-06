package com.example.sebastia_carlos_proyectodi.data

import com.example.sebastia_carlos_proyectodi.data.local.TiendaEntity
import com.example.sebastia_carlos_proyectodi.data.remote.TiendaDto
import com.example.sebastia_carlos_proyectodi.domain.model.Tienda

fun TiendaDto.toEntity(): TiendaEntity = TiendaEntity(
    id = this.id.toInt(),
    ciudad = this.ciudad ?: "",
    direccion = this.direccion ?: "",
    imagenUrl = this.imagen ?: ""
)

fun TiendaEntity.toDomain(): Tienda = Tienda(
    id = this.id,
    ciudad = this.ciudad,
    direccion = this.direccion,
    imagenUrl = this.imagenUrl
)