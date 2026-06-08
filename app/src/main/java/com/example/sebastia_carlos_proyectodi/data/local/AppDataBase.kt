package com.example.sebastia_carlos_proyectodi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [
    UsuarioEntity::class,
    ProductoEntity::class,
    TiendaEntity::class,
    ListaEntity::class,
    PreferenciasEntity::class],
    version = 5, exportSchema = false)
@TypeConverters(Utils::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun productoDao(): ProductoDao
    abstract fun tiendaDao(): TiendaDao
    abstract fun listaDao(): ListaDao
    abstract fun preferenciasDao(): PreferenciasDao

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