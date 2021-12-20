package ru.l4gunner4l.decideclothes.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ru.l4gunner4l.decideclothes.base.ListItem
import ru.l4gunner4l.decideclothes.local.AppDatabase
import ru.l4gunner4l.decideclothes.local.COLUMNS
import java.util.*

@Entity(
    tableName = AppDatabase.SALES_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = KindEntity::class,
            parentColumns = [COLUMNS.KIND_ID],
            childColumns = [COLUMNS.SALE_KIND_ID],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.RESTRICT
        )
    ]
)
@Parcelize
data class SaleEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMNS.SALE_ID)
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = COLUMNS.SALE_NAME)
    val name: String,
    @ColumnInfo(name = COLUMNS.SALE_KIND_ID)
    val kindId: String
): Parcelable

@Parcelize
data class Sale(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val kind: KindEntity,
    val goods: List<Good>
): Parcelable, ListItem

@Parcelize
data class DetailsSale(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val kind: KindEntity,
    val goods: List<CheckedGood>
): Parcelable, ListItem

fun Sale.toEntity() = SaleEntity(id, name, kind.id)

fun Sale.toDetailsSale(isChecked: Boolean) =
    DetailsSale(id, name, kind, goods.map { it.toEntity().toCheckedGood(isChecked) })