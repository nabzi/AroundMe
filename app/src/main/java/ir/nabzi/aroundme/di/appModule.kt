package ir.nabzi.aroundme.di

import com.google.gson.Gson
import ir.nabzi.aroundme.data.repository.PlaceRepository
import ir.nabzi.aroundme.data.repository.PlaceRepositoryImpl
import ir.nabzi.aroundme.ui.home.PlaceViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single { Gson() }

    //Repository
    single<PlaceRepository> { PlaceRepositoryImpl(get(), get()) }

    //Viewmodel
    viewModel { PlaceViewModel(get()) }
}