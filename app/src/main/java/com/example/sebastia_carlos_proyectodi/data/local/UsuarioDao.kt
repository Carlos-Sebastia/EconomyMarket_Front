package com.example.sebastia_carlos_proyectodi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sebastia_carlos_proyectodi.domain.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE dni = :dni LIMIT 1")
    suspend fun obtenerUsuarioPorDni(dni: String): UsuarioEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios LIMIT 1")
    fun obtenerUsuarioLogueado(): Flow<UsuarioEntity?>

    @Query("DELETE FROM usuarios")
    suspend fun borrarUsuarios()
}