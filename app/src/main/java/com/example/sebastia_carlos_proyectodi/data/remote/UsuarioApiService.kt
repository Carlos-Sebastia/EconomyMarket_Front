package com.example.sebastia_carlos_proyectodi.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface UsuarioApiService {
    @GET("login")
    suspend fun login(
        @Query("dni") dni: String,
        @Query("contrasena") contrasena: String
    ): UsuarioDto?
}