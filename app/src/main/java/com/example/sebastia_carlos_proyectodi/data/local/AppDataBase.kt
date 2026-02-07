package com.example.sebastia_carlos_proyectodi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [
    ProductoEntity::class,
    TiendaEntity::class,
    ListaEntity::class],
    version = 2, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun tiendaDao(): TiendaDao
    abstract fun listaDao(): ListaDao

    companion object {
        @Volatile
        private var Instance: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDataBase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}