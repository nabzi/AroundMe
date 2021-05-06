package ir.nabzi.aroundme

import ir.nabzi.aroundme.data.remote.*
import ir.nabzi.aroundme.model.NetworkCall
import ir.nabzi.aroundme.model.Place
import ir.nabzi.aroundme.model.Resource
import ir.nabzi.aroundme.model.Status
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ApiServiceTest {
    ////https://foursquare.com/developers/explore#req=venues%2Fexplore%3Fnear%3DTehran
    lateinit var apiServices : ApiService
    @Before
    fun setup(){
       apiServices=createService(createRetrofit(createHttpClient(), BASE_URL))
    }

    @Test
    fun getPlacesList_success() = runBlocking {
        var result: Resource<List<Place>> = object  : NetworkCall<List<Place>>(){
            override suspend fun createCall(): Response<List<Place>> {
                return apiServices.getPlaceList()
            }
        }.fetch()
        assert(result.status == Status.SUCCESS)
    }
}