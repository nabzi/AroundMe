package ir.nabzi.aroundme.ui.home

import android.location.Location
import androidx.lifecycle.*
import com.mapbox.mapboxsdk.geometry.LatLng
import ir.nabzi.aroundme.data.repository.PlaceRepository
import ir.nabzi.aroundme.model.Place
import ir.nabzi.aroundme.model.Resource
import kotlinx.coroutines.flow.*

class PlaceViewModel(placeRepository: PlaceRepository) : ViewModel() {
    val currentLocation = MutableLiveData(LatLng(0.0, 0.0))//MutableStateFlow<LatLng>(LatLng(35.702, 51.3380464))
    var placeList =
            currentLocation.switchMap {
                placeRepository.getPlacesNearLocation(it.latitude, it.longitude, viewModelScope, true)
                        .asLiveData()
            }

    val selectedPlaceId = MutableLiveData<String>()
    val place = selectedPlaceId.map { _id ->
        placeList.value?.data?.firstOrNull { it.id == _id }
    }

}
