package ru.l4gunner4l.decideclothes.local.dao

import androidx.room.*
import ru.l4gunner4l.decideclothes.local.AppDatabase.Companion.KINDS_TABLE
import ru.l4gunner4l.decideclothes.local.COLUMNS
import ru.l4gunner4l.decideclothes.local.entity.KindEntity

@Dao
interface KindsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun create(entity: KindEntity)

    @Update
    suspend fun update(kind: KindEntity)

    @Query("SELECT * FROM $KINDS_TABLE WHERE ${COLUMNS.KIND_ID} = :id")
    suspend fun read(id: String): KindEntity

    @Query("SELECT * FROM $KINDS_TABLE")
    suspend fun readAll(): List<KindEntity>

    @Delete
    suspend fun delete(kind: KindEntity)



    @Query("SELECT * FROM $KINDS_TABLE WHERE ${COLUMNS.KIND_NAME} LIKE '%' || :name || '%'")
    suspend fun searchByName(name: String): List<KindEntity>

}