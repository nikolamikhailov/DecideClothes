package ru.l4gunner4l.decideclothes.local.dao

import androidx.room.*
import ru.l4gunner4l.decideclothes.local.AppDatabase
import ru.l4gunner4l.decideclothes.local.COLUMNS
import ru.l4gunner4l.decideclothes.local.entity.SaleEntity

@Dao
interface SalesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun create(entity: SaleEntity)

    @Update
    suspend fun update(entity: SaleEntity)

    @Query("SELECT * FROM ${AppDatabase.SALES_TABLE}")
    suspend fun readAll(): List<SaleEntity>

    @Delete
    suspend fun delete(sale: SaleEntity)


    @Query("SELECT * FROM ${AppDatabase.SALES_TABLE} WHERE ${COLUMNS.SALE_NAME} LIKE '%' || :query || '%'")
    suspend fun searchByName(query: String) : List<SaleEntity>

}
