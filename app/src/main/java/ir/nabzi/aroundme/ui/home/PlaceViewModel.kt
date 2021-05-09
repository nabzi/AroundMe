package ir.nabzi.aroundme.ui.home

import androidx.lifecycle.*
import ir.nabzi.aroundme.data.repository.PlaceRepository
import ir.nabzi.aroundme.model.Place
import ir.nabzi.aroundme.model.Resource
import kotlinx.coroutines.flow.StateFlow

class PlaceViewModel(placeRepository: PlaceRepository) : ViewModel() {
    var placeList : StateFlow<Resource<List<Place>>?> =  placeRepository.getPlacesNearLocation(viewModelScope,true)
    val selectedPlaceId = MutableLiveData<String>()
    val place = selectedPlaceId.map { _id ->
        placeList.value?.data?.firstOrNull { it.id == _id }
    }
}
