package ru.l4gunner4l.decideclothes.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.l4gunner4l.decideclothes.local.AppDatabase.Companion.USERS_TABLE
import ru.l4gunner4l.decideclothes.local.entity.UserEntity

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun create(entity: UserEntity)

    @Query("SELECT * FROM $USERS_TABLE WHERE email = :email")
    suspend fun read(email: String): UserEntity?

}

