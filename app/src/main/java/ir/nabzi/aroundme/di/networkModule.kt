package ir.nabzi.places.di

import android.icu.util.TimeUnit
import ir.nabzi.places.BuildConfig
import ir.nabzi.places.BuildConfig.DEBUG
import ir.nabzi.places.data.remote.BASE_URL
import ir.nabzi.places.data.remote.createHttpClient
import ir.nabzi.places.data.remote.createRetrofit
import ir.nabzi.places.data.remote.createService
import ir.nabzi.places.model.Place
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

val networkModule = module {
    single { createService(get()) }
    single { createHttpClient() }
    single { createRetrofit(get(), BASE_URL) }
}

