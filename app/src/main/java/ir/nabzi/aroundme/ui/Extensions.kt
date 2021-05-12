package ir.nabzi.aroundme.ir.nabzi.aroundme.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.location.LocationManager
import android.widget.Toast
import androidx.core.location.LocationManagerCompat
import androidx.fragment.app.Fragment
import com.mapbox.mapboxsdk.geometry.LatLng


fun Fragment.showError(message: String?) {
    Toast.makeText(requireContext(), message ?: "error", Toast.LENGTH_SHORT).show()
}

fun Context.requireLocationEnabled() {
    if (!locationEnabled())
        AlertDialog.Builder(this)
                .setTitle("Location needed")
                .setMessage("Please enable location service")
                .setPositiveButton("ok", DialogInterface.OnClickListener { dialog, which -> })
                .show()

}

fun Context.locationEnabled(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return LocationManagerCompat.isLocationEnabled(locationManager)
}
fun Activity.saveLocation(latLng: LatLng){
    val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
    with (sharedPref.edit()) {
        putFloat("lat", latLng.latitude.toFloat())
        putFloat("lon", latLng.longitude.toFloat())
        apply()
    }
}

fun Activity.getLastLocation(): LatLng? {
    val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return null
    return  LatLng(sharedPref.getFloat("lat", 0f).toDouble() , sharedPref.getFloat("lon", 0f).toDouble())
}