package com.example.sebastia_carlos_proyectodi.domain.repository

import com.example.sebastia_carlos_proyectodi.domain.model.Notificacion
import kotlinx.coroutines.flow.Flow

interface NotificacionRepository {
    fun getNotificaciones(dni: String): Flow<List<Notificacion>>
    suspend fun enviarNotificacion(notificacion: Notificacion)
    suspend fun borrarNotificacion(id: Long)
}