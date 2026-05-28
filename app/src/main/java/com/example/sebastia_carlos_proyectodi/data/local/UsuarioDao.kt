package com.example.sebastia_carlos_proyectodi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE dni = :dni LIMIT 1")
    suspend fun obtenerUsuarioPorDni(dni: String): UsuarioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: UsuarioEntity)
}