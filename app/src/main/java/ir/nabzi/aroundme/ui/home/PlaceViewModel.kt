package ir.nabzi.aroundme.ui.home

import androidx.lifecycle.*
import ir.nabzi.aroundme.data.repository.PlaceRepository
import ir.nabzi.aroundme.data.model.Place
import ir.nabzi.aroundme.data.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PlaceViewModel(placeRepository: PlaceRepository) : ViewModel() {
    val placeList : MutableStateFlow<Resource<List<Place>>?> = MutableStateFlow(Resource.loading(null))
    init{
        viewModelScope.launch(Dispatchers.IO) {
            placeList.value =  placeRepository.getPlaces(true).value
        }
    }
    val selectedPlaceId = MutableLiveData<String>()
    val place = selectedPlaceId.map { _id ->
        placeList.value?.data?.firstOrNull { it.id == _id }
    }
}
