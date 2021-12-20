package ru.l4gunner4l.decideclothes.local.dao

import androidx.room.*
import ru.l4gunner4l.decideclothes.local.AppDatabase.Companion.GOODS_TABLE
import ru.l4gunner4l.decideclothes.local.COLUMNS
import ru.l4gunner4l.decideclothes.local.COLUMNS.GOOD_NAME
import ru.l4gunner4l.decideclothes.local.entity.GoodEntity

@Dao
interface GoodsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun create(entity: GoodEntity)

    @Update
    suspend fun update(entity: GoodEntity)

    @Query("SELECT * FROM $GOODS_TABLE WHERE ${COLUMNS.GOOD_ID} = :id")
    suspend fun read(id: String): GoodEntity

    @Query("SELECT * FROM $GOODS_TABLE")
    suspend fun readAll(): List<GoodEntity>

    @Delete
    suspend fun delete(good: GoodEntity)



    @Query("SELECT * FROM $GOODS_TABLE WHERE $GOOD_NAME LIKE '%' || :query || '%'")
    suspend fun searchByName(query: String): List<GoodEntity>


    @Query("SELECT * FROM $GOODS_TABLE WHERE ${COLUMNS.GOOD_KIND_ID} = :kindId")
    suspend fun searchByKindId(kindId: String): List<GoodEntity>

}