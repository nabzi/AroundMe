package ir.nabzi.aroundme.data.repository

import ir.nabzi.aroundme.data.db.PlaceDao
import ir.nabzi.aroundme.data.remote.ApiService
import ir.nabzi.aroundme.model.NetworkCall
import ir.nabzi.aroundme.model.Place
import ir.nabzi.aroundme.model.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response

interface PlaceRepository {
    suspend fun getPlaces(
        shouldFetch: Boolean
    ): StateFlow<Resource<List<Place>>?>
}

class PlaceRepositoryImpl(
    val placeDao: PlaceDao,
    val apiServices: ApiService
) : PlaceRepository {

    override suspend fun getPlaces(shouldFetch: Boolean): StateFlow<Resource<List<Place>>?> {
        var stateFlow: MutableStateFlow<Resource<List<Place>>?> =
            MutableStateFlow(Resource.loading(null))
        stateFlow.emit(
            object : RemoteResource<List<Place>>() {
                override suspend fun updateDB(result: List<Place>) {
                    updateDBSource(result)
                }

                override fun getFromDB(): List<Place> {
                    return getPlacesFromDBSource()
                }

                override suspend fun pullFromServer(): Resource<List<Place>> {
                    return getPlacesFromRemoteSource()
                }
            }.get(true)
        )
        return stateFlow
    }

    /**Data Sources:
     * This functions can be placed in classes
     * PlaceDBSource and PlaceRemoteSource if they are large
     * and added as dependency for repository
    **/
    private suspend fun updateDBSource(result : List<Place>) {
        placeDao.addList(result)
    }

    private fun getPlacesFromDBSource(): List<Place> {
        return placeDao.getPlaces()
    }

    private suspend fun getPlacesFromRemoteSource(): Resource<List<Place>> {
        return object : NetworkCall<List<Place>>() {
            override suspend fun createCall(): Response<List<Place>> {
                return apiServices.getPlaceList()
            }
        }.fetch()
    }

}