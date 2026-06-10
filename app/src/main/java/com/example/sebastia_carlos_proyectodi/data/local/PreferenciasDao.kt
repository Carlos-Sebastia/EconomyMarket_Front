package com.example.sebastia_carlos_proyectodi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PreferenciasDao {
    @Query("SELECT * FROM preferencias_usuario")
    fun getTodasLasPreferencias(): Flow<List<PreferenciasEntity>>
    @Query("SELECT * FROM preferencias_usuario WHERE usuarioDni = :dni")
    fun getPreferencias(dni: String): Flow<PreferenciasEntity?>

    //Guarda los datos de las preferencias en la tabla. En caso de ya existir datos, los reemplaza
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarPreferencias(preferencias: PreferenciasEntity)

}