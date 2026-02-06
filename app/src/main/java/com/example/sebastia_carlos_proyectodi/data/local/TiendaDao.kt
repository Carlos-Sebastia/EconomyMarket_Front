package com.example.sebastia_carlos_proyectodi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TiendaDao {
    @Query("SELECT * FROM tiendas")
    fun getAllTiendas(): Flow<List<TiendaEntity>>

    @Query("SELECT * FROM tiendas")
    suspend fun getAllTiendasOnce(): List<TiendaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTiendas(tiendas: List<TiendaEntity>)

    @Query("DELETE FROM tiendas")
    suspend fun deleteAllTiendas()
}