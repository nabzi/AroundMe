package ir.nabzi.aroundme.util

import android.content.Context
import com.mapbox.mapboxsdk.geometry.LatLng

class SharedPrefHelper(val context: Context) {
     val SHARED_PREF = "aroundme"
     val LAT = "lat"
     val LON = "lon"

    fun saveLocation(latLng: LatLng){
        val sharedPref = context.getSharedPreferences( SHARED_PREF, Context.MODE_PRIVATE ) ?: return
        with (sharedPref.edit()) {
            putFloat(LAT, latLng.latitude.toFloat())
            putFloat(LON, latLng.longitude.toFloat())
            apply()
        }
    }

    fun getLastLocation(): LatLng {
        val sharedPref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE) ?: return LatLng(0.0,0.0)
        return  LatLng(sharedPref.getFloat(LAT, 0f).toDouble() , sharedPref.getFloat(LON, 0f).toDouble())
    }
}