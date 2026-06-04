package com.example.sebastia_carlos_proyectodi.data.remote

import com.google.gson.annotations.SerializedName

class NotificacionDto (
    @SerializedName("id") val id: Long,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("dniUsuario") val dniUsuario: String
)