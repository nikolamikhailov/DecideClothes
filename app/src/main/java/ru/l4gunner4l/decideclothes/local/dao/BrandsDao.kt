package ru.l4gunner4l.decideclothes.local.dao

import androidx.room.*
import ru.l4gunner4l.decideclothes.local.AppDatabase.Companion.BRANDS_TABLE
import ru.l4gunner4l.decideclothes.local.COLUMNS.BRAND_ID
import ru.l4gunner4l.decideclothes.local.COLUMNS.BRAND_NAME
import ru.l4gunner4l.decideclothes.local.entity.BrandEntity

@Dao
interface BrandsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun create(entity: BrandEntity)

    @Update
    suspend fun update(brand: BrandEntity)

    @Query("SELECT * FROM $BRANDS_TABLE WHERE $BRAND_ID = :id")
    suspend fun read(id: String): BrandEntity

    @Query("SELECT * FROM $BRANDS_TABLE")
    suspend fun readAll(): List<BrandEntity>

    @Delete
    suspend fun delete(brandEntity: BrandEntity)



    @Query("SELECT * FROM $BRANDS_TABLE WHERE $BRAND_NAME LIKE '%' || :query || '%'")
    suspend fun searchByName(query: String): List<BrandEntity>

}