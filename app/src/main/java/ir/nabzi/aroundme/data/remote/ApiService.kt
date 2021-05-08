package ir.nabzi.aroundme.data.remote

import ir.nabzi.aroundme.model.Place
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

   // @GET("/venues/explore?near=Tehran&client_id=P4A13A4DPSF1IFZK4RH0QW4OVQAXFZ0MGOY1ZXVSMAK1V2BI&client_secret=5VDXFX5RLEQQQF4IYZDAZAIV5WQL4J3HL4WPL0OMZAV43XRA")

    @GET("/api/v1/places")
    suspend  fun getPlaceList(): Response<List<Place>>



}