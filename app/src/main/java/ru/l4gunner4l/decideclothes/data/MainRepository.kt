package ru.l4gunner4l.decideclothes.data

import android.database.sqlite.SQLiteConstraintException
import ru.l4gunner4l.decideclothes.local.dao.*
import ru.l4gunner4l.decideclothes.local.entity.*

class MainRepository(
    private val goodsDao: GoodsDao,
    private val brandsDao: BrandsDao,
    private val kindsDao: KindsDao,
    private val salesDao: SalesDao,
    private val sgDao: SalesGoodsDao
) {

    suspend fun addGood(good: GoodEntity) {
        goodsDao.create(good)
    }

    suspend fun updateGood(good: GoodEntity) {
        goodsDao.update(good)
    }

    suspend fun readGoodById(id: String): GoodEntity {
        return goodsDao.read(id)
    }

    suspend fun readAllGoods(): List<GoodEntity> {
        return goodsDao.readAll()
    }

    suspend fun deleteGood(good: GoodEntity): Boolean {
        return try {
            goodsDao.delete(good)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

    suspend fun searchByKindId(id: String): List<GoodEntity> {
        return goodsDao.searchByKindId(id)
    }

    suspend fun searchGoods(query: String): List<GoodEntity> {
        return goodsDao.searchByName(query)
    }




    suspend fun addBrand(brand: BrandEntity) {
        brandsDao.create(brand)
    }

    suspend fun updateBrand(brand: BrandEntity) {
        brandsDao.update(brand)
    }

    suspend fun readAllBrands(): List<BrandEntity> {
        return brandsDao.readAll()
    }

    suspend fun readBrandById(id: String): BrandEntity {
        return brandsDao.read(id)
    }

    suspend fun deleteBrand(brandEntity: BrandEntity): Boolean {
        return try {
            brandsDao.delete(brandEntity)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }
    }

    suspend fun searchBrands(query: String): List<BrandEntity> {
        return brandsDao.searchByName(query)
    }





    suspend fun addKind(kind: KindEntity) {
        kindsDao.create(kind)
    }

    suspend fun updateKind(kind: KindEntity) {
        kindsDao.update(kind)
    }

    suspend fun readAllKinds(): List<KindEntity> {
        return kindsDao.readAll()
    }
    suspend fun readKindById(id: String): KindEntity {
        return kindsDao.read(id)
    }

    suspend fun deleteKind(kind: KindEntity): Boolean {
        return try {
            kindsDao.delete(kind)
            true
        } catch (e: SQLiteConstraintException) {
            false
        }

    }

    suspend fun searchKinds(query: String): List<KindEntity> {
        return kindsDao.searchByName(query)
    }




    suspend fun addSale(sale: SaleEntity) {
        salesDao.create(sale)
    }

    suspend fun updateSale(sale: SaleEntity) {
        salesDao.update(sale)
    }

    suspend fun readAllSales(): List<SaleEntity> {
        return salesDao.readAll()
    }

    suspend fun delete(sale: SaleEntity) {
        return salesDao.delete(sale)
    }

    suspend fun searchSales(query: String): List<SaleEntity> {
        return salesDao.searchByName(query)
    }




    suspend fun addSaleGood(sg: SaleGoodEntity) {
        sgDao.create(sg)
    }

    suspend fun updateSaleGood(sg: SaleGoodEntity) {
        sgDao.update(sg)
    }

    suspend fun deleteSaleGood(sg: SaleGoodEntity) {
        sgDao.delete(sg)
    }

    suspend fun readGoodsIdsBySaleId(saleId: String): List<String> {
        return sgDao.searchBySaleId(saleId).map { it.goodId }
    }

    suspend fun deleteSaleGoodsBySaleId(saleId: String) {
        sgDao.deleteBySaleId(saleId)
    }

}