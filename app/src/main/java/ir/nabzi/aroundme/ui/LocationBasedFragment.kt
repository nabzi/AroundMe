package ir.nabzi.aroundme.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*

abstract class LocationBasedFragment : Fragment() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 111
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onResume() {
        super.onResume()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        startLocationUpdates()
        registerLocationServiceDetection()

    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        unRegisterLocationServiceDetection()
    }


    private fun registerLocationServiceDetection(){
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
        requireContext().registerReceiver(locationSwitchStateReceiver, filter)
    }
    private fun unRegisterLocationServiceDetection(){
        requireContext().unregisterReceiver(locationSwitchStateReceiver)
    }
    private var locationSwitchStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (LocationManager.PROVIDERS_CHANGED_ACTION == intent.action) {
                checkGPS()
            }
        }
    }

    fun checkGPS() {

        val locationManager =  requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsEnabled){
            locationEnabled()
        } else {
            locationDisabled()
        }
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
            ActivityCompat.requestPermissions(
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
                Looper.getMainLooper()
            )

        }

    }
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                onLocationUpdated(location)
            }
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted
            }
        }
    }

    abstract fun locationEnabled()

    abstract fun locationDisabled()

    abstract fun onLocationUpdated(location : Location)
}