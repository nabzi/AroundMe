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
                    getImageUrl(poi.classifications[0].code ),
                    score.toInt(),
                    position.lat,
                    position.lon
            )
        }
    }

    private fun getImageUrl(code: String): String? {
        return when(code){
            "SCHOOL" ->  "http://riyainfosystems.com/wp-content/uploads/2019/06/5b76b45cbb3a6.jpg"
            "PARK_RECREATION_AREA" -> "https://www.hamburg.de/image/3242800/16x9/990/557/19bcf03f011e6d1477b4e4f17be8a7a5/Cd/bild-fuer-einleitungstext.jpg"
            "HOTEL_MOTEL" -> "https://www.epersianhotel.com/images/other-hotel/kish/toranj/dbl-room1.jpg"
            "MARKET" -> "https://www.epersianhotel.com/images/other-hotel/kish/toranj/view7.jpg"
            "HEALTH_CARE_SERVICE" ->"https://www.healthcareitnews.com/sites/hitn/files/120319%20CaroMont%20Regional%20Medical%20Center%20712.jpg"
            else -> "https://cdn.britannica.com/s:690x388,c:crop/12/130512-004-AD0A7CA4/campus-Riverside-Ottawa-The-Hospital-Ont.jpg"
        }
    }
}