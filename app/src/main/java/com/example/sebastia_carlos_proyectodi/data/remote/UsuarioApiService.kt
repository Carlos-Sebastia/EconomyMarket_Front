package com.example.sebastia_carlos_proyectodi.data.remote

import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface UsuarioApiService {
    @GET("login")
    suspend fun login(
        @Query("dni") dni: String,
        @Query("contrasena") contrasena: String
    ): UsuarioDto?

    @GET("login/validarMascota")
    suspend fun validarMascota(
        @Query("dni") dni: String,
        @Query("mascota") mascota: String
    ): UsuarioDto?

    @PUT("login/cambiarContrasena")
    suspend fun cambiarContrasena(
        @Query("dni") dni: String,
        @Query("nuevaContrasena") nuevaContrasena: String
    ): Boolean

    @PUT("login/crearUsuario")
    suspend fun crearUsuario(
        @Query("dni") dni: String,
        @Query("nombre") nombre: String,
        @Query("apellidos") apellidos: String,
        @Query("fecha_nacimiento") fechaNacimiento: String,
        @Query("email") email: String,
        @Query("mascota") mascota: String,
        @Query("contrasena") contrasena: String
    ): Boolean
}