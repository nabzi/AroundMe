package ir.nabzi.aroundme.ui

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

/*** Check location service enabled */
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
