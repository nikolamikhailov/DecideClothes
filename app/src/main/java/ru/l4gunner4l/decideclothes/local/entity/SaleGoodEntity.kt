package ru.l4gunner4l.decideclothes.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ru.l4gunner4l.decideclothes.local.AppDatabase
import ru.l4gunner4l.decideclothes.local.COLUMNS
import java.util.*

@Entity(
    tableName = AppDatabase.SALES_GOODS_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = GoodEntity::class,
            parentColumns = [COLUMNS.GOOD_ID],
            childColumns = [COLUMNS.SG_GOOD_ID],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = SaleEntity::class,
            parentColumns = [COLUMNS.SALE_ID],
            childColumns = [COLUMNS.SG_SALE_ID],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        )
    ])
data class SaleGoodEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMNS.SG_ID)
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = COLUMNS.SG_SALE_ID)
    val saleId: String,
    @ColumnInfo(name = COLUMNS.SG_GOOD_ID)
    val goodId: String
) {
}

@Parcelize
data class SaleGood(
    val sale: Sale,
    val good: Good
) : Parcelable