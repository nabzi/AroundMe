package ir.nabzi.aroundme.di

import ir.nabzi.aroundme.BuildConfig
import ir.nabzi.aroundme.BuildConfig.DEBUG
import ir.nabzi.aroundme.data.remote.BASE_URL
import ir.nabzi.aroundme.data.remote.createHttpClient
import ir.nabzi.aroundme.data.remote.createRetrofit
import ir.nabzi.aroundme.data.remote.createService
import org.koin.dsl.module

val networkModule = module {
    single { createService(get()) }
    single { createHttpClient() }
    single { createRetrofit(get(), BASE_URL) }
}

