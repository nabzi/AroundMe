package ir.nabzi.aroundme.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.TransitionOptions
import ir.nabzi.aroundme.R
import ir.nabzi.aroundme.ir.nabzi.aroundme.ui.MAP_ACCESS_TOKEN
import ir.nabzi.aroundme.ir.nabzi.aroundme.ui.showError
import ir.nabzi.aroundme.model.Place
import ir.nabzi.aroundme.model.Status
import kotlinx.android.synthetic.main.fragment_places.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.sharedViewModel


class PlacesFragment : Fragment() {
    private val vmodel: PlaceViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Mapbox.getInstance(requireContext(), MAP_ACCESS_TOKEN)
        return inflater.inflate(R.layout.fragment_places, container, false)
    }

    private fun subscribeUi() {
        this.lifecycleScope.launch {
            vmodel.placeList.collect { resource ->
                resource?.data?.let {
                    if (it.isNotEmpty())
                        showPlaces(it)
                }
                when(resource?.status){
                    Status.SUCCESS ->   showProgress(false)
                    Status.ERROR -> {
                        showProgress(false)
                        showError(resource.message)
                    }
                    Status.LOADING -> showProgress(true)
                }
            }
        }
    }

    private fun showProgress(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showPlaces(places: List<Place>) {
        initViewPager(places)
        initMap(places)
    }

    private fun initMap(places: List<Place>) {
        mapView.getMapAsync(OnMapReadyCallback { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS
            ) { style ->
                style.transition = TransitionOptions(0, 0, false);
                val position = CameraPosition.Builder()
                    .target(LatLng(places[1].location_lat, places[1].location_lng))
                    .zoom(15.0)
                    .build()
                mapboxMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(position),
                    1000
                );
                for (place in places)
                    mapboxMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(place.location_lat, place.location_lng))
                            .title(place.id)
                    )
                mapboxMap.setOnMarkerClickListener { it ->
                    selectPlace(it.title)
                    true
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState);
        subscribeUi()
    }

    private fun initViewPager(places: List<Place>) {
        viewPager?.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                 return PlaceItemFragment.create(
                    places[position]
                ) { id ->
                     vmodel.selectedPlaceId.postValue(id)
                    findNavController().navigate(
                        PlacesFragmentDirections.actionPlacesFragmentToPlaceDetailsFragment()
                    )
                }


            }

            override fun getItemCount(): Int {
                return places.size
            }
        }
    }

    private fun selectPlace(title: String?) {
        title?.toIntOrNull()?.let {
            viewPager.setCurrentItem(it - 1)
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        mapView?.onDestroy()
        super.onDestroy()

    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}
