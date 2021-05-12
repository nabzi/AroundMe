package ir.nabzi.aroundme

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mapbox.mapboxsdk.geometry.LatLng
import ir.nabzi.aroundme.ir.nabzi.aroundme.ui.getLastLocation
import ir.nabzi.aroundme.ir.nabzi.aroundme.ui.saveLocation
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPrefTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
       appContext.saveLocation(LatLng(1.0,3.0))
        val ll = appContext.getLastLocation()
        Assert.assertEquals(3.0 , ll.longitude)

    }
}