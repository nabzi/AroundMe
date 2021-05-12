package ir.nabzi.aroundme

import ir.nabzi.aroundme.data.remote.*
import ir.nabzi.aroundme.data.repository.NetworkCall
import ir.nabzi.aroundme.model.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class ApiServiceTest {
    lateinit var apiServices : ApiService
    @Before
    fun setup(){
       apiServices=createService(createRetrofit(createHttpClient(), BASE_URL))
    }

    @Test
    fun getPlacesList_success() = runBlocking {
        var result: Resource<List<PointResponse>> = object  : NetworkCall<List<PointResponse>>(){
            override suspend fun createCall(): Response<List<PointResponse>> {
                return apiServices.getPlaceList(35.78,51.35 , API_KEY)
            }
        }.fetch()
        assert(result.status == Status.SUCCESS)
    }
}