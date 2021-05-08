package ir.nabzi.aroundme.ui.home

import androidx.lifecycle.*
import ir.nabzi.aroundme.data.repository.PlaceRepository
import ir.nabzi.aroundme.model.Place
import ir.nabzi.aroundme.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaceViewModel(placeRepository: PlaceRepository) : ViewModel() {
    var placeList : StateFlow<Resource<List<Place>>?> =  placeRepository.getPlaces(viewModelScope,true)
    val selectedPlaceId = MutableLiveData<String>()
    val place = selectedPlaceId.map { _id ->
        placeList.value?.data?.firstOrNull { it.id == _id }
    }
}
