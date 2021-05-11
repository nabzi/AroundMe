package ir.nabzi.aroundme.ui.home

import androidx.lifecycle.*
import com.mapbox.mapboxsdk.geometry.LatLng
import ir.nabzi.aroundme.data.repository.PlaceRepository

class PlaceViewModel(private val placeRepository: PlaceRepository) : ViewModel() {
    val currentLocation = MutableLiveData<LatLng>()
    private var  page = 1

    var placeList =
        currentLocation.switchMap {
            page = 1
            placeRepository.getPlacesNearLocation(it.latitude, it.longitude, viewModelScope, 1 , true)
                    .asLiveData()
        }
    var hasMorePages = placeList.map {
        it?.hasMore
    }
    val selectedPlaceId = MutableLiveData<String>()
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

    fun onLocationUpdateReceived(latLng: LatLng) {
        currentLocation.value.let {
            if ( it == null || (latLng.distanceTo(it) > 100))
                currentLocation.postValue(latLng)
        }
    }
}
