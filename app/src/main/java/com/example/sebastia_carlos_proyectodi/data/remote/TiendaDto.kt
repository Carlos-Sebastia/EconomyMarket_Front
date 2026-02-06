package com.example.sebastia_carlos_proyectodi.data.remote

import com.google.gson.annotations.SerializedName

data class TiendaDto(
    @SerializedName("id") val id: Long,
    @SerializedName("ciudad") val ciudad: String,
    @SerializedName("direccion") val direccion: String,
    @SerializedName("urlImagen") val imagen: String?
)