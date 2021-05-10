package ir.nabzi.aroundme.ui.home

import androidx.lifecycle.*
import com.mapbox.mapboxsdk.geometry.LatLng
import ir.nabzi.aroundme.data.repository.PlaceRepository

class PlaceViewModel(val placeRepository: PlaceRepository) : ViewModel() {
    val currentLocation = MutableLiveData(LatLng(0.0, 0.0))
    fun loadMorePlaces() {
        page.value = page.value?.plus(1)
        currentLocation.value?.let {
            placeRepository.getPlacesNearLocation(it.latitude, it.longitude, viewModelScope, page.value ?: 1, true)
        }
    }

    var placeList =
            currentLocation.switchMap {
                page.postValue(1)
                placeRepository.getPlacesNearLocation(it.latitude, it.longitude, viewModelScope, 1 , true)
                        .asLiveData()
            }

    val selectedPlaceId = MutableLiveData<String>()
    val place = selectedPlaceId.map { _id ->
        placeList.value?.data?.firstOrNull { it.id == _id }
    }
    val page = MutableLiveData<Int>(0)

}
