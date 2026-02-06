package com.example.sebastia_carlos_proyectodi.data.remote

import com.google.gson.annotations.SerializedName

data class ProductoDto(
    @SerializedName("id") val id: Long,
    @SerializedName("nombre") val nombre : String,
    @SerializedName("precio") val precio : String,
    @SerializedName("imagenNombre") val imagen : String?,
    @SerializedName("categoria") val categoria : String
)