package ir.nabzi.aroundme

import android.app.Application
import ir.nabzi.aroundme.di.appModule
import ir.nabzi.aroundme.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {
    override fun onCreate(){
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    appModule,
                    networkModule,
                    dbModule
                )
            )
        }
//        if(BuildConfig.DEBUG) {
//            Stetho.initializeWithDefaults(this)
//        }
    }
}