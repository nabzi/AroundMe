package ir.nabzi.aroundme.data.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.nabzi.aroundme.data.db.PlaceDao
import ir.nabzi.aroundme.model.Place

/**
 * Main database description.
 */
@Database(
    entities = [
        Place::class],
    version = 1,
    exportSchema = false
)
abstract class DB : RoomDatabase() {

    abstract fun placeDao(): PlaceDao

    companion object {
        private var instance: DB? = null

        @Synchronized
        fun get(context: Context): DB {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    DB::class.java, "AppDatabase"
                )
                    .build()
            }
            return instance!!
        }
    }
}
