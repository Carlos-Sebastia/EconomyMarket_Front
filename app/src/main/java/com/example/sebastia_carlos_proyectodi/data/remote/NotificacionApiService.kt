package com.example.sebastia_carlos_proyectodi.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificacionApiService {
    @GET("notificaciones")
    suspend fun getNotificacionesByDni(
        @Query("dni") dni: String
    ): List<NotificacionDto>

    @POST("notificaciones")
    suspend fun crearNotificacion(
        @Body notificacion: NotificacionDto): NotificacionDto

    @DELETE("notificaciones/{id}")
    suspend fun borrarNotificacion(@Path("id") id: Long)
}