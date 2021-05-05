package ir.nabzi.aroundme

import android.app.Application


class App : Application() {
    override fun onCreate(){
        super.onCreate()
//        startKoin {
//            androidContext(this@App)
//            modules(
//                listOf(
//                    appModule,
//                    networkModule,
//                    dbModule
//                )
//            )
//        }
    }
}