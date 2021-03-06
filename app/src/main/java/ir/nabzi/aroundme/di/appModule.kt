package ir.nabzi.aroundme.di

import com.google.gson.Gson
import ir.nabzi.aroundme.data.repository.PlaceDBDataSource
import ir.nabzi.aroundme.data.repository.PlaceRemoteDataSource
import ir.nabzi.aroundme.data.repository.PlaceRepository
import ir.nabzi.aroundme.data.repository.PlaceRepositoryImpl
import ir.nabzi.aroundme.ui.home.PlaceViewModel
import ir.nabzi.aroundme.util.SharedPrefHelper
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single                  { Gson()                            }

    //Repository
    single<PlaceRepository> { PlaceRepositoryImpl(get(), get()) }

    //DataSource
    single                  { PlaceRemoteDataSource(get())      }
    single                  { PlaceDBDataSource(get())          }

    //Viewmodel
    viewModel               { PlaceViewModel(get() , get() )     }

    single                  { SharedPrefHelper (get())          }
}