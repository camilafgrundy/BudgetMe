package com.ucsm.budgetme.data

import androidx.room.*

@Dao
interface TransaccionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(transaccion: Transaccion): Long

    @Update
    suspend fun actualizar(transaccion: Transaccion): Int

    @Delete
    suspend fun eliminar(transaccion: Transaccion): Int

    @Query("SELECT * FROM transacciones WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): Transaccion?

    @Query("SELECT * FROM transacciones ORDER BY id DESC")
    fun listarTodos(): List<Transaccion>
}