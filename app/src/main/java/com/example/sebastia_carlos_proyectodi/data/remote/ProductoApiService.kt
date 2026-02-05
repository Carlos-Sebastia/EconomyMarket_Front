package com.example.sebastia_carlos_proyectodi.data.remote

import retrofit2.http.GET

interface ProductoApiService {
    @GET("productos")
    suspend fun getProductos(): List<ProductoDto>
}

