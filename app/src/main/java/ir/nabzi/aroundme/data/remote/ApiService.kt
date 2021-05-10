package ir.nabzi.aroundme.data.remote

import ir.nabzi.aroundme.model.NearbySearchResponse
import ir.nabzi.aroundme.model.Place
import ir.nabzi.aroundme.model.PointResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/search/2/nearbySearch/.json")
    suspend  fun getPlaceList(@Query("lat")lat : Double,
                              @Query("lon") lon : Double,
                              @Query("key")key : String = API_KEY)
            : Response<NearbySearchResponse>
}