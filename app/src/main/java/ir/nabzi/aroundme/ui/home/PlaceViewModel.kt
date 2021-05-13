package ir.nabzi.aroundme.ui.home

import androidx.lifecycle.*
import com.mapbox.mapboxsdk.geometry.LatLng
import ir.nabzi.aroundme.data.repository.PlaceRepository
import ir.nabzi.aroundme.util.DataStoreHelper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlaceViewModel(private val placeRepository  : PlaceRepository,
                     private val dataStoreHelper : DataStoreHelper) : ViewModel(){

    var  currentLocation : MutableLiveData<LatLng> = MutableLiveData()
    val  selectedPlaceId = MutableLiveData<String>()
    private var  page = 1
    val MIN_LOCATION_CHANGE = 100
    lateinit var lastLocation : LatLng
    init{
        runBlocking {
            dataStoreHelper.getLastLocation().collect {
                lastLocation = it
            }
        }
    }
    var placeList = currentLocation.switchMap {
            page = 1
            placeRepository.getPlacesNearLocation(it.latitude, it.longitude, viewModelScope, 1 ,
                lastLocation.distanceTo(it) > MIN_LOCATION_CHANGE)
                    .asLiveData()
    }

    var hasMorePages = placeList.map {
        it?.hasMore
    }

    val place = selectedPlaceId.map { _id ->
        placeList.value?.data?.firstOrNull { it.id == _id }
    }

    fun loadMorePlaces() {
        if(hasMorePages.value == false)
            return
        page++
        currentLocation.value?.let {
            placeRepository.getPlacesNearLocation(it.latitude, it.longitude, viewModelScope, page, true)
        }
    }

    fun onLocationReceived(latLng: LatLng) {
        currentLocation.value.let {
            it?.let {
                viewModelScope.launch {
                    dataStoreHelper.saveLocation(it)
                }
            }
            if ( it == null || (latLng.distanceTo(it) > MIN_LOCATION_CHANGE)) {
                currentLocation.postValue(latLng)

            }
        }
    }
}
