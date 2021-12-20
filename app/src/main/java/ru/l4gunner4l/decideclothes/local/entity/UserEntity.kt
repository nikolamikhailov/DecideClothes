package ru.l4gunner4l.decideclothes.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ru.l4gunner4l.decideclothes.local.AppDatabase
import ru.l4gunner4l.decideclothes.local.COLUMNS

@Parcelize
@Entity(tableName = AppDatabase.USERS_TABLE)
data class UserEntity(
        @PrimaryKey
        @ColumnInfo(name = COLUMNS.EMAIL)
        val email: String,
        @ColumnInfo(name = COLUMNS.PASSWORD)
        val password: String
): Parcelable