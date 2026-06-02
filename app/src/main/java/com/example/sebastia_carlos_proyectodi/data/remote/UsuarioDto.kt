package com.example.sebastia_carlos_proyectodi.data.remote

import com.google.gson.annotations.SerializedName

data class UsuarioDto(
    @SerializedName("dni") val dni: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellidos") val apellidos: String,
    @SerializedName("email") val email: String,
    @SerializedName("fecha_nacimiento") val fechaNacimiento: String,
    @SerializedName("mascota") val mascota: String,
    @SerializedName("contrasena") val contrasena: String
)
