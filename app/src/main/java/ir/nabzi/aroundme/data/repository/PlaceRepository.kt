package ir.nabzi.aroundme.data.repository

import ir.nabzi.aroundme.data.db.PlaceDao
import ir.nabzi.aroundme.data.remote.ApiService
import ir.nabzi.aroundme.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response

interface PlaceRepository {
    fun getPlacesNearLocation(
            lat: Double, lon: Double,
            coroutineScope: CoroutineScope,
            shouldFetch: Boolean
    ): StateFlow<Resource<List<Place>>?>
}

class PlaceRepositoryImpl(
        val placeDao: PlaceDao,
        val apiServices: ApiService
) : PlaceRepository {

    override fun getPlacesNearLocation(lat: Double, lon: Double, coroutineScope: CoroutineScope, shouldFetch: Boolean)
            : StateFlow<Resource<List<Place>>?> {
        return object : RemoteResource<List<Place>>() {
            override suspend fun updateDB(result: List<Place>) {
                updateDBSource(result)
            }

            override fun getFromDB(): Flow<List<Place>> {
                return getPlacesFromDBSource()
            }

            override suspend fun pullFromServer(): Resource<List<Place>> {
                return getPlacesFromRemoteSource(lat, lon)
            }
        }.get(coroutineScope, true)
    }

    /**Data Sources:
     * This functions can be placed in classes
     * PlaceDBSource and PlaceRemoteSource if they are large
     * and added as dependency for repository
     **/
    private suspend fun updateDBSource(result: List<Place>) {
        placeDao.addList(result)
    }

    private fun getPlacesFromDBSource(): Flow<List<Place>> {
        return placeDao.getPlacesFlow()
    }

    private suspend fun getPlacesFromRemoteSource(lat: Double, lon: Double): Resource<List<Place>> {
        val res = object : NetworkCall<NearbySearchResponse>() {
            override suspend fun createCall(): Response<NearbySearchResponse> {
                return apiServices.getPlaceList(lat, lon)
            }
        }.fetch()
        return Resource(res.status,
                res.data?.results?.map { pointResponseToPlace(it) }, res.message, res.errorCode)
    }

    private fun pointResponseToPlace(pointResponse: PointResponse): Place {
        pointResponse.run {
            return Place(
                    id,
                    poi.name,
                    null,
                    "",
                    score.toInt(),
                    position.lat,
                    position.lon
            )
        }
    }
}