package ir.nabzi.aroundme.ui.home

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
import ir.nabzi.aroundme.model.Place
import ir.nabzi.aroundme.model.Status
import ir.nabzi.aroundme.ui.LocationBasedFragment
import ir.nabzi.aroundme.ui.home.adapter.PlaceAdapter
import ir.nabzi.aroundme.ui.requireLocationEnabled
import ir.nabzi.aroundme.ui.showError
import kotlinx.android.synthetic.main.fragment_places.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


class PlacesFragment : LocationBasedFragment() {

    private val vmodel: PlaceViewModel by sharedViewModel()
    private var mapboxMap: MapboxMap? = null
    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Mapbox.getInstance(requireContext(), MAP_ACCESS_TOKEN)
        return inflater.inflate(R.layout.fragment_places, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
        mapView.onCreate(savedInstanceState);
        subscribeUi()
        checkGPS()
    }

    private fun subscribeUi() {
        vmodel.placeList.observe(viewLifecycleOwner, Observer { resource ->
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
        })
        vmodel.currentLocation.observe(viewLifecycleOwner, Observer {
            setCamera(it)
        })

        vmodel.hasMorePages.observe(viewLifecycleOwner, Observer {
            adapter.isMoreDataAvailable = it ?: true
        })
    }


    private fun showProgress(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showProgressLoadMore(show: Boolean) {
        progressBarLoadMore.visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun showPlaces(places: List<Place>) {
        if(places.isEmpty())
            return
        adapter.apply {
            list = places
            isMoreDataAvailable = true
            notifyDataChanged()
        }
        initMap(places)
    }

    private fun initAdapter() {
        adapter = object : PlaceAdapter({ id -> goToPlace(id) }) {
            override fun loadMore(lastItem: Place?) {
                vmodel.loadMorePlaces()
            }
        }
        rvPlace.adapter = adapter
        adapter.isLoading.observe(viewLifecycleOwner, Observer {
            showProgressLoadMore(it)
        })
    }

    fun goToPlace(id: String) {
        vmodel.selectedPlaceId.postValue(id)
        this.findNavController().navigate(
            PlacesFragmentDirections.actionPlacesFragmentToPlaceDetailsFragment()
        )
    }

    override fun onLocationUpdated(location: Location) {
        LatLng(location.latitude, location.longitude).run{
            vmodel.onLocationReceived(this)
            setCamera(this)
        }
    }


    override fun locationEnabled() {
        tvGpsStatus.visibility = View.GONE
    }

    override fun locationDisabled() {
        tvGpsStatus.visibility = View.VISIBLE
    }


    /** Map functions */
    private fun initMap(places: List<Place>) {
        mapView.getMapAsync(OnMapReadyCallback { mapboxMap ->
            mapboxMap.setStyle(
                Style.MAPBOX_STREETS
            ) { style ->
                style.transition = TransitionOptions(0, 0, false);
                this.mapboxMap = mapboxMap
                for (place in places)
                    mapboxMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(place.location_lat, place.location_lng))
                            .title(place.id)
                    )
            }
        })
    }

    private fun setCamera(latLng: LatLng) {
        val position = CameraPosition.Builder()
            .target(latLng)
            .zoom(12.0)
            .build()
        mapboxMap?.animateCamera(
            CameraUpdateFactory.newCameraPosition(position),
            1000
        )
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
