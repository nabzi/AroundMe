package ir.nabzi.aroundme

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mapbox.mapboxsdk.geometry.LatLng
import ir.nabzi.aroundme.util.DataStoreHelper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataStoreTest {
    @Test
    fun saveAndReadDataInDatastore() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dataStore = DataStoreHelper(appContext)
        runBlocking {
            dataStore.saveLocation(LatLng(1.0, 3.0))

            dataStore.getLastLocation().collect {
                Assert.assertEquals(3.0 , it.longitude)
            }
        }


    }
}