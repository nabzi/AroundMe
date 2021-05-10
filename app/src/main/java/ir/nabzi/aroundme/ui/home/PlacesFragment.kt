package ir.nabzi.aroundme.ui.home

import android.Manifest
import android.content.ClipData.Item
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.TransitionOptions
import ir.nabzi.aroundme.R
import ir.nabzi.aroundme.ir.nabzi.aroundme.ui.MAP_ACCESS_TOKEN
import ir.nabzi.aroundme.ir.nabzi.aroundme.ui.showError
import ir.nabzi.aroundme.model.Place
import ir.nabzi.aroundme.model.Status
import ir.nabzi.aroundme.ui.home.adapter.PlaceAdapter
import kotlinx.android.synthetic.main.fragment_places.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.sharedViewModel


class PlacesFragment : Fragment() {
    private val vmodel: PlaceViewModel by sharedViewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val LOCATION_PERMISSION_REQUEST_CODE = 111
    private lateinit var adapter: PlaceAdapter
    val onLocationUpdated = { location: Location ->
        Toast.makeText(requireContext(), "location updated" + location?.latitude + "," + location?.longitude, Toast.LENGTH_SHORT).show()
        setCamera(location)
    }
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                onLocationUpdated(location)
            }
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        Mapbox.getInstance(requireContext(), MAP_ACCESS_TOKEN)
        return inflater.inflate(R.layout.fragment_places, container, false)
    }

    private fun subscribeUi() {
        this.lifecycleScope.launch {
            whenStarted {
                vmodel.placeList.collect { resource ->
                    resource?.data?.let {
                        if (it.isNotEmpty())
                            showPlaces(it)
                    }
                    when (resource?.status) {
                        Status.SUCCESS -> showProgress(false)
                        Status.ERROR -> {
                            showProgress(false)
                            showError(resource.message)
                        }
                        Status.LOADING -> showProgress(true)
                    }
                }
            }
        }
    }

    private fun showProgress(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showPlaces(places: List<Place>) {
       adapter.submitList(places)
        initMap(places)
    }

    var mapboxMap: MapboxMap? = null
    private fun initMap(places: List<Place>) {
        mapView.getMapAsync(OnMapReadyCallback { mapboxMap ->
            mapboxMap.setStyle(
                    Style.MAPBOX_STREETS
            ) { style ->
                style.transition = TransitionOptions(0, 0, false);
                this.mapboxMap = mapboxMap
//                setCamera(mapboxMap)
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

    private fun selectPlace(title: String?) {

    }

    private fun setCamera(location: Location) {

        val position = CameraPosition.Builder()
                .target(LatLng(location.latitude, location.longitude))
                .zoom(15.0)
                .build()
        mapboxMap?.animateCamera(
                CameraUpdateFactory.newCameraPosition(position),
                1000
        )
        //todo : add places markers
//        mapboxMap?.addMarker(
//                MarkerOptions()
//                        .position(LatLng(location.latitude, location.longitude)))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = object : PlaceAdapter(requireContext(), {id ->  goToPlace(id)}){
            override fun loadMore(lastItem: Place) {
                //TODO("Not yet implemented")
                Toast.makeText(requireContext(),"load more" , Toast.LENGTH_SHORT).show()
            }
        }
        rvPlace.adapter = adapter
        mapView.onCreate(savedInstanceState);
        subscribeUi()
    }

    fun goToPlace(id: String){
        vmodel.selectedPlaceId.postValue(id)
        this.findNavController().navigate(
            PlacesFragmentDirections.actionPlacesFragmentToPlaceDetailsFragment()
        )
    }



    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                    requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ),
                    LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            //permission granted
            val locationRequest = LocationRequest().apply {
                fastestInterval = 5000
                interval = 10000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper())

        }

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
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
