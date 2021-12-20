package ru.l4gunner4l.decideclothes.local

import androidx.room.*
import ru.l4gunner4l.decideclothes.local.dao.*
import ru.l4gunner4l.decideclothes.local.entity.*

object COLUMNS {

    const val EMAIL = "email"
    const val PASSWORD = "password"

    const val GOOD_ID = "good_id"
    const val GOOD_NAME = "good_name"
    const val GOOD_PRICE = "good_price"
    const val GOOD_IMAGE = "good_image"
    const val GOOD_KIND_ID = "good_kind_id"
    const val GOOD_BRAND_ID = "good_brand_id"

    const val KIND_ID = "kind_id"
    const val KIND_NAME = "kind_name"

    const val BRAND_ID = "brand_id"
    const val BRAND_NAME = "brand_name"
    const val BRAND_IMAGE = "brand_image"

    const val SALE_ID = "sale_id"
    const val SALE_NAME = "sale_name"
    const val SALE_KIND_ID = "sale_kind_id"

    const val SG_ID = "sg_id"
    const val SG_SALE_ID = "sg_sale_id"
    const val SG_GOOD_ID = "sg_good_id"
}

@Database(
    entities = [
        UserEntity::class,
        GoodEntity::class,
        KindEntity::class,
        BrandEntity::class,
        SaleEntity::class,
        SaleGoodEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun goodsDao(): GoodsDao
    abstract fun kindsDao(): KindsDao
    abstract fun brandsDao(): BrandsDao
    abstract fun salesDao(): SalesDao
    abstract fun sgDao(): SalesGoodsDao

    companion object {
        const val DB_NAME = "DB_NAME_DECIDE_CLOTHES"
        const val USERS_TABLE = "USERS_TABLE"
        const val GOODS_TABLE = "GOODS_TABLE"
        const val KINDS_TABLE = "KINDS_TABLE"
        const val BRANDS_TABLE = "BRANDS_TABLE"
        const val SALES_TABLE = "SALES_TABLE"
        const val SALES_GOODS_TABLE = "SALES_GOODS_TABLE"
    }
}

