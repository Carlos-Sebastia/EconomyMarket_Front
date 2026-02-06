package com.example.sebastia_carlos_proyectodi

import AppContainer
import DefaultAppContainer
import android.app.Application

class MyApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}