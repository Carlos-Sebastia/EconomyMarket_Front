package com.example.sebastia_carlos_proyectodi.data.remote

import retrofit2.http.GET

interface TiendaApiService {
    @GET("tiendas")
    suspend fun getTiendas(): List<TiendaDto>
}