package com.example.sebastia_carlos_proyectodi.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ListaDao {

    @Query("SELECT * FROM lista")
    fun getTodasLasEntradas(): Flow<List<ListaEntity>>

    @Query("SELECT productoId FROM lista WHERE usuarioDni = :usuarioDni")
    fun getIdsEnLista(usuarioDni: String): Flow<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(item: ListaEntity)

    @Delete
    suspend fun eliminar(item: ListaEntity)

    @Query("DELETE FROM lista WHERE usuarioDni = :usuarioDni")
    suspend fun vaciarLista(usuarioDni: String)
}