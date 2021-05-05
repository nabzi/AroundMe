package ir.nabzi.aroundme

import android.app.Application
import androidx.room.Room
import ir.nabzi.aroundme.data.db.PlaceDao
import ir.nabzi.arpoundme.data.db.DB
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dbModule = module {
    fun provideDatabase(application: Application): DB {
        return Room.databaseBuilder(application, DB::class.java, "place").build()
    }

    fun providePlaceDao(database: DB): PlaceDao {
        return database.placeDao()
    }

    single { provideDatabase(androidApplication()) }

    single { providePlaceDao(get()) }
}