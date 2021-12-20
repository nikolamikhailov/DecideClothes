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

@Entity(tableName = AppDatabase.BRANDS_TABLE)
@Parcelize
data class BrandEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMNS.BRAND_ID)
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = COLUMNS.BRAND_NAME)
    val name: String,
    @ColumnInfo(name = COLUMNS.BRAND_IMAGE)
    val image: String
): ListItem, Parcelable