package ru.l4gunner4l.decideclothes.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.l4gunner4l.decideclothes.data.MainRepository
import ru.l4gunner4l.decideclothes.local.AppDatabase
import ru.l4gunner4l.decideclothes.local.AppDatabase.Companion.DB_NAME
import ru.l4gunner4l.decideclothes.ui.sign.LoginViewModel
import ru.l4gunner4l.decideclothes.ui.sign.RegisterViewModel
import ru.l4gunner4l.decideclothes.data.SignRepository
import ru.l4gunner4l.decideclothes.local.dao.*
import ru.l4gunner4l.decideclothes.local.entity.*
import ru.l4gunner4l.decideclothes.ui.admin.brands.BrandsViewModel
import ru.l4gunner4l.decideclothes.ui.admin.brands.details.DetailsBrandViewModel
import ru.l4gunner4l.decideclothes.ui.admin.goods.GoodsViewModel
import ru.l4gunner4l.decideclothes.ui.admin.goods.details.DetailsGoodViewModel
import ru.l4gunner4l.decideclothes.ui.admin.kinds.KindsViewModel
import ru.l4gunner4l.decideclothes.ui.admin.kinds.details.DetailsKindViewModel
import ru.l4gunner4l.decideclothes.ui.admin.sales.SalesViewModel
import ru.l4gunner4l.decideclothes.ui.admin.sales.details.DetailsSaleViewModel
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

const val CLOTHES_QUALIFIER = "CLOTHES_QUALIFIER"

val navModule = module {

    single<Cicerone<Router>>(named(CLOTHES_QUALIFIER)){
        Cicerone.create()
    }

    single<NavigatorHolder>(named(CLOTHES_QUALIFIER)) {
        get<Cicerone<Router>>(named(CLOTHES_QUALIFIER)).navigatorHolder
    }

    single<Router>(named(CLOTHES_QUALIFIER)) {
        get<Cicerone<Router>>(named(CLOTHES_QUALIFIER)).router
    }
}

val signModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, DB_NAME)
            .build()
    }
    single {
        get<AppDatabase>().usersDao()
    }
    single<SignRepository> {
        SignRepository(get())
    }
    viewModel {
        LoginViewModel(get())
    }
    viewModel {
        RegisterViewModel(get())
    }
}

val adminModule = module {
    single<GoodsDao> {
        get<AppDatabase>().goodsDao()
    }
    single<SalesDao> {
        get<AppDatabase>().salesDao()
    }
    single<BrandsDao> {
        get<AppDatabase>().brandsDao()
    }
    single<KindsDao> {
        get<AppDatabase>().kindsDao()
    }
    single<SalesGoodsDao> {
        get<AppDatabase>().sgDao()
    }
    single<MainRepository> {
        MainRepository(get(), get(), get(), get(), get())
    }
    viewModel<GoodsViewModel> {
        GoodsViewModel(get(), get(named(CLOTHES_QUALIFIER)))
    }
    viewModel<BrandsViewModel> {
        BrandsViewModel(get(), get(named(CLOTHES_QUALIFIER)))
    }
    viewModel<KindsViewModel> {
        KindsViewModel(get(), get(named(CLOTHES_QUALIFIER)))
    }
    viewModel<SalesViewModel> {
        SalesViewModel(get(), get(named(CLOTHES_QUALIFIER)))
    }
    viewModel<DetailsGoodViewModel> { (good: Good?) ->
        DetailsGoodViewModel(good, get(), get(named(CLOTHES_QUALIFIER)))
    }
    viewModel<DetailsBrandViewModel> { (brand: BrandEntity?) ->
        DetailsBrandViewModel(brand, get(), get(named(CLOTHES_QUALIFIER)))
    }
    viewModel<DetailsKindViewModel> { (kind: KindEntity?) ->
        DetailsKindViewModel(kind, get(), get(named(CLOTHES_QUALIFIER)))
    }
    viewModel<DetailsSaleViewModel> { (sale: Sale?) ->
        DetailsSaleViewModel(sale, get(), get(named(CLOTHES_QUALIFIER)))
    }
}


