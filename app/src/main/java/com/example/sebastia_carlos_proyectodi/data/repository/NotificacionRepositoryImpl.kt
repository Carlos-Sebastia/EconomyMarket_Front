package com.example.sebastia_carlos_proyectodi.data.repository

import android.util.Log
import com.example.sebastia_carlos_proyectodi.data.remote.NotificacionApiService
import com.example.sebastia_carlos_proyectodi.data.toDomain
import com.example.sebastia_carlos_proyectodi.data.toDto
import com.example.sebastia_carlos_proyectodi.domain.model.Notificacion
import com.example.sebastia_carlos_proyectodi.domain.repository.NotificacionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class NotificacionRepositoryImpl(
    private val api: NotificacionApiService
) : NotificacionRepository {

    override fun getNotificaciones(dni: String): Flow<List<Notificacion>> = flow {
        while (true) {
            try {
                val response = api.getNotificacionesByDni(dni)
                emit(response.map { it.toDomain()})
            } catch (e: Exception) {
                Log.e("NotifRepo", "Error al obtener notificaciones: ${e.message}")
                emit(emptyList())
            }
            delay(5000)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun enviarNotificacion(notificacion: Notificacion) {
        withContext(Dispatchers.IO) {
            try {
                api.crearNotificacion(notificacion.toDto())
            } catch (e: Exception) {
                Log.e("NotifRepo", "Error al enviar notificación: ${e.message}")            }
        }
    }

    override suspend fun borrarNotificacion(id: Long) {
        withContext(Dispatchers.IO) {
            try {
                api.borrarNotificacion(id)
            } catch (e: Exception) {
                Log.e("NotifRepo", "Error al borrar notificación: ${e.message}")            }
        }
    }
}
