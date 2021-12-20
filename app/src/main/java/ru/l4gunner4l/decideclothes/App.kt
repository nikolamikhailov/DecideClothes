package ru.l4gunner4l.decideclothes

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.l4gunner4l.decideclothes.di.adminModule
import ru.l4gunner4l.decideclothes.di.navModule
import ru.l4gunner4l.decideclothes.di.signModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(navModule, signModule, adminModule)
        }
    }
}