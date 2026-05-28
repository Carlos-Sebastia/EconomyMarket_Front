package com.example.sebastia_carlos_proyectodi.data

import com.example.sebastia_carlos_proyectodi.data.local.UsuarioEntity
import com.example.sebastia_carlos_proyectodi.data.remote.UsuarioDto
import com.example.sebastia_carlos_proyectodi.domain.model.Usuario
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun UsuarioDto.toEntity(): UsuarioEntity {
    return UsuarioEntity(
        dni = dni,
        nombre = nombre,
        apellidos = apellidos,
        email = email,
        fechaNacimiento = fechaNacimiento
    )
}

fun UsuarioDto.toDomain(): Usuario {
    return Usuario(
        dni = dni,
        nombre = nombre,
        apellidos = apellidos,
        email = email,
        fechaNacimiento = try {
            LocalDate.parse(fechaNacimiento)
        } catch (e: Exception) {
            LocalDate.now()
        }
    )
}
