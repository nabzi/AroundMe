package ir.nabzi.aroundme.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.nabzi.aroundme.model.Place
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun add(vararg place: Place)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addList(list: List<Place>)

    @Query("SELECT * FROM place " )
    abstract fun getPlaces(): List<Place>

    @Query("SELECT * FROM place " )
    abstract fun getPlacesFlow(): Flow<List<Place>>

    @Query("Delete From place" )
    abstract suspend fun removeAll()
}