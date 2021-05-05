package ir.nabzi.aroundme.di

import android.icu.util.TimeUnit
import ir.nabzi.aroundme.BuildConfig
import ir.nabzi.aroundme.BuildConfig.DEBUG
import ir.nabzi.aroundme.data.remote.BASE_URL
import ir.nabzi.aroundme.data.remote.createHttpClient
import ir.nabzi.aroundme.data.remote.createRetrofit
import ir.nabzi.aroundme.data.remote.createService
import ir.nabzi.aroundme.model.Place
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

