package ir.nabzi.aroundme.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import androidx.fragment.app.Fragment

abstract class LocationBasedFragment : Fragment() {

    override fun onResume() {
        super.onResume()

        registerLocationServiceDetection()

    }

    override fun onPause() {
        super.onPause()
        unRegisterLocationServiceDetection()
    }

    fun registerLocationServiceDetection(){
        val filter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        filter.addAction(Intent.ACTION_PROVIDER_CHANGED)
        requireContext().registerReceiver(locationSwitchStateReceiver, filter)
    }
    fun unRegisterLocationServiceDetection(){
        requireContext().unregisterReceiver(locationSwitchStateReceiver)
    }
    var locationSwitchStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
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

    abstract fun locationEnabled()

    abstract fun locationDisabled()

}