package ir.nabzi.aroundme.data.repository

import ir.nabzi.aroundme.data.db.PlaceDao
import ir.nabzi.aroundme.data.remote.ApiService
import ir.nabzi.aroundme.data.repository.PlaceRepositoryImpl.Companion.PAGE_SIZE
import ir.nabzi.aroundme.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response

interface PlaceRepository {
    fun getPlacesNearLocation(
        lat: Double, lon: Double,
        coroutineScope: CoroutineScope,
        page: Int,
        shouldFetch: Boolean
    ): StateFlow<Resource<List<Place>>?>
}

class PlaceRepositoryImpl(
    private val dbDataSource: PlaceDBDataSource,
    private val remoteDataSource: PlaceRemoteDataSource
) : PlaceRepository {
    companion object {
        val PAGE_SIZE = 20
    }

    override fun getPlacesNearLocation(
        lat: Double,
        lon: Double,
        coroutineScope: CoroutineScope,
        page: Int,
        shouldFetch: Boolean
    )
            : StateFlow<Resource<List<Place>>?> {
        return object : RemoteResource<List<Place>>() {
            override suspend fun updateDB(result: List<Place>) {
                if (page == 1)
                    dbDataSource.clear()
                dbDataSource.update(result)
            }

            override fun getFromDB(): Flow<List<Place>> {
                return dbDataSource.getPlaces()
            }

            override suspend fun pullFromServer(): Resource<List<Place>> {
                return remoteDataSource.getPlacesFromRemoteSource(lat, lon, page)
            }
        }.get(coroutineScope, true)
    }
}

class PlaceDBDataSource(private val placeDao: PlaceDao) {
    suspend fun update(result: List<Place>) {
        placeDao.addList(result)
    }

    suspend fun clear() {
        placeDao.removeAll()
    }

    fun getPlaces(): Flow<List<Place>> {
        return placeDao.getPlacesFlow()
    }
}

class PlaceRemoteDataSource(val apiServices: ApiService) {
    suspend fun getPlacesFromRemoteSource(lat: Double, lon: Double, page: Int): Resource<List<Place>> {
        val res = object : NetworkCall<NearbySearchResponse>() {
            override suspend fun createCall(): Response<NearbySearchResponse> {
                return apiServices.getPlaceList(lat, lon, PAGE_SIZE, (page - 1) * PAGE_SIZE)
            }
        }.fetch()
        var result = res.data?.let { pointResponseToPlaces(it) }
        return Resource(
            res.status,
           result?.first, res.message, res.errorCode , result?.second
        )
    }

    private fun pointResponseToPlaces(nearbySearchResponse: NearbySearchResponse): Pair<List<Place>, Boolean> {
        return Pair(nearbySearchResponse.results.map { pointResponse ->
            pointResponse.run {
                Place(
                    id,
                    poi.name,
                    null,
                    getImageUrl(poi.classifications[0].code),
                    score.toInt(),
                    position.lat,
                    position.lon
                )
            }
        }, nearbySearchResponse.summary.let {
            it.offset <= it.totalResults - PAGE_SIZE
        })

    }

    private fun getImageUrl(code: String): String? {
        return when (code) {
            "SCHOOL" -> "http://riyainfosystems.com/wp-content/uploads/2019/06/5b76b45cbb3a6.jpg"
            "PARK_RECREATION_AREA" -> "https://www.hamburg.de/image/3242800/16x9/990/557/19bcf03f011e6d1477b4e4f17be8a7a5/Cd/bild-fuer-einleitungstext.jpg"
            "HOTEL_MOTEL" -> "https://www.epersianhotel.com/images/other-hotel/kish/toranj/dbl-room1.jpg"
            "MARKET" -> "https://www.epersianhotel.com/images/other-hotel/kish/toranj/view7.jpg"
            "HEALTH_CARE_SERVICE" -> "https://www.healthcareitnews.com/sites/hitn/files/120319%20CaroMont%20Regional%20Medical%20Center%20712.jpg"
            else -> "https://cdn.britannica.com/s:690x388,c:crop/12/130512-004-AD0A7CA4/campus-Riverside-Ottawa-The-Hospital-Ont.jpg"
        }
    }
}