package ru.l4gunner4l.decideclothes.local.dao

import androidx.room.*
import ru.l4gunner4l.decideclothes.local.AppDatabase.Companion.SALES_GOODS_TABLE
import ru.l4gunner4l.decideclothes.local.COLUMNS.SG_SALE_ID
import ru.l4gunner4l.decideclothes.local.entity.SaleGoodEntity

@Dao
interface SalesGoodsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun create(entity: SaleGoodEntity)

    @Update
    suspend fun update(entity: SaleGoodEntity)

    @Query("SELECT * FROM $SALES_GOODS_TABLE")
    suspend fun readAll(): List<SaleGoodEntity>

    @Delete
    suspend fun delete(sale: SaleGoodEntity)


    @Query("SELECT * FROM $SALES_GOODS_TABLE WHERE $SG_SALE_ID = :saleId")
    suspend fun searchBySaleId(saleId: String) : List<SaleGoodEntity>

    @Query("DELETE FROM $SALES_GOODS_TABLE WHERE $SG_SALE_ID = :saleId")
    suspend fun deleteBySaleId(saleId: String)

}