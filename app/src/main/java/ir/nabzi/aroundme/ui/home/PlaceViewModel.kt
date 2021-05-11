package ir.nabzi.aroundme.ui.home

import androidx.lifecycle.*
import com.mapbox.mapboxsdk.geometry.LatLng
import ir.nabzi.aroundme.data.repository.PlaceRepository

class PlaceViewModel(val placeRepository: PlaceRepository) : ViewModel() {
    val currentLocation = MutableLiveData<LatLng>()//MutableStateFlow<LatLng>(LatLng(35.702, 51.3380464))
    var  page = 1
    fun loadMorePlaces() {
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

    var placeList =
            currentLocation.switchMap {
                page = 1
                placeRepository.getPlacesNearLocation(it.latitude, it.longitude, viewModelScope, 1 , true)
                        .asLiveData()
            }

    val selectedPlaceId = MutableLiveData<String>()
    val place = selectedPlaceId.map { _id ->
        placeList.value?.data?.firstOrNull { it.id == _id }
    }
}
