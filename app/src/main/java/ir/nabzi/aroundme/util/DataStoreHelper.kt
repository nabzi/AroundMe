package ir.nabzi.aroundme.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "last_data")

class DataStoreHelper(val context: Context) {
    val LATLNG = stringPreferencesKey("lat_lng")
    suspend fun saveLocation(latLng: LatLng){
        context.dataStore.edit { settings ->
            settings[LATLNG] = Gson().toJson(latLng)
        }
    }

    fun getLastLocation(): Flow<LatLng> {
        return context.dataStore.data
            .map { preferences ->
                Gson().fromJson(preferences[LATLNG] ?: "", LatLng::class.java)
            }
    }
}