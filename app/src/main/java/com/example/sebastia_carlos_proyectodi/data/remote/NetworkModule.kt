package com.example.sebastia_carlos_proyectodi.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetroditInstance {
    private const val BASE_URL = "http://10.0.2.2:8080"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val productoService: ProductoApiService by lazy {
        retrofit.create(ProductoApiService::class.java)
    }

    val tiendaService: TiendaApiService by lazy {
        retrofit.create(TiendaApiService::class.java)
    }
}