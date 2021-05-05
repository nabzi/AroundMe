package ir.nabzi.aroundme.data.remote

import ir.nabzi.aroundme.data.model.Place
import retrofit2.Response

interface ApiService {
    abstract fun getPlaceList(): Response<List<Place>>


}