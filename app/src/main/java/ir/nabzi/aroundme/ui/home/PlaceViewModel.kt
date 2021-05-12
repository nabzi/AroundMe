package ir.nabzi.aroundme.ui.home

import android.content.Context
import androidx.lifecycle.*
import com.mapbox.mapboxsdk.geometry.LatLng
import ir.nabzi.aroundme.data.repository.PlaceRepository
import ir.nabzi.aroundme.util.SharedPrefHelper

class PlaceViewModel(private val placeRepository: PlaceRepository, private val sharedPrefHelper : SharedPrefHelper) : ViewModel(){

    var  currentLocation : MutableLiveData<LatLng> = MutableLiveData()
    val  selectedPlaceId = MutableLiveData<String>()
    private var  page = 1
    val MIN_LOCATION_CHANGE = 100
    var lastLocation = sharedPrefHelper.getLastLocation()

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
                sharedPrefHelper.saveLocation(it)
            }
            if ( it == null || (latLng.distanceTo(it) > MIN_LOCATION_CHANGE)) {
                currentLocation.postValue(latLng)

            }
        }
    }
}
