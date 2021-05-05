package ir.nabzi.aroundme.data.remote

import ir.nabzi.aroundme.model.Place
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    abstract fun getPlaceList(): Response<List<Place>>


}