package com.example.sebastia_carlos_proyectodi.data

import com.example.sebastia_carlos_proyectodi.data.remote.NotificacionDto
import com.example.sebastia_carlos_proyectodi.domain.model.Notificacion
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun NotificacionDto.toDomain(): Notificacion {
    return Notificacion(
        id = id,
        titulo = titulo,
        mensaje = mensaje,
        fecha = fecha,
        dniUsuario = dniUsuario
    )
}

fun Notificacion.toDto(): NotificacionDto {
    return NotificacionDto(
        id = id,
        titulo = titulo,
        mensaje = mensaje,
        fecha = fecha,
        dniUsuario = dniUsuario
    )
}
fun String.formatearFechaNotificacion(): String {
    return try {
        val fechaNotif = LocalDateTime.parse(this)
        val ahora = LocalDateTime.now()

        if (fechaNotif.toLocalDate() == ahora.toLocalDate()) {
            fechaNotif.format(DateTimeFormatter.ofPattern("HH:mm"))
        } else {
            fechaNotif.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }
    } catch (e: Exception) {
        this
    }
}