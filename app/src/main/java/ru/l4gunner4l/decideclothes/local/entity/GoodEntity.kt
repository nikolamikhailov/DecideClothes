package ru.l4gunner4l.decideclothes.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.RESTRICT
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import ru.l4gunner4l.decideclothes.base.ListItem
import ru.l4gunner4l.decideclothes.local.AppDatabase
import ru.l4gunner4l.decideclothes.local.COLUMNS
import java.util.*

@Parcelize
@Entity(
    tableName = AppDatabase.GOODS_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = BrandEntity::class,
            parentColumns = [COLUMNS.BRAND_ID],
            childColumns = [COLUMNS.GOOD_BRAND_ID],
            onDelete = RESTRICT,
            onUpdate = RESTRICT
        ),
        ForeignKey(
            entity = KindEntity::class,
            parentColumns = [COLUMNS.KIND_ID],
            childColumns = [COLUMNS.GOOD_KIND_ID],
            onDelete = RESTRICT,
            onUpdate = RESTRICT
        )
    ]
)
data class GoodEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMNS.GOOD_ID)
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = COLUMNS.GOOD_NAME)
    val name: String,
    @ColumnInfo(name = COLUMNS.GOOD_BRAND_ID)
    val brandId: String,
    @ColumnInfo(name = COLUMNS.GOOD_KIND_ID)
    val kindId: String,
    @ColumnInfo(name = COLUMNS.GOOD_PRICE)
    val price: Int,
    @ColumnInfo(name = COLUMNS.GOOD_IMAGE)
    val image: String
) : Parcelable

@Parcelize
data class Good(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val brand: BrandEntity,
    val kind: KindEntity,
    val price: Int,
    val image: String
) : ListItem, Parcelable

fun Good.toEntity() = GoodEntity(id, name, brand.id, kind.id, price, image)

@Parcelize
data class CheckedGood(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val image: String,
    var isChecked: Boolean = false
) : Parcelable

fun GoodEntity.toCheckedGood(isChecked: Boolean) = CheckedGood(id, name, image, isChecked)

