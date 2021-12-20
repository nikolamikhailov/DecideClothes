package ru.l4gunner4l.decideclothes.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ru.l4gunner4l.decideclothes.base.ListItem
import ru.l4gunner4l.decideclothes.local.AppDatabase
import ru.l4gunner4l.decideclothes.local.COLUMNS
import java.util.*

@Entity(tableName = AppDatabase.KINDS_TABLE)
@Parcelize
data class KindEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMNS.KIND_ID)
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = COLUMNS.KIND_NAME)
    val name: String
): ListItem, Parcelable