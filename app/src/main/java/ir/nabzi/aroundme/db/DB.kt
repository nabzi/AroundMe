package ir.nabzi.arpoundme.data.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.nabzi.aroundme.data.db.PlaceDao
import ir.nabzi.aroundme.data.model.Place

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
//                    .addCallback(object : RoomDatabase.Callback() {
//                        override fun onCreate(db: SupportSQLiteDatabase) {
//                            fillInDb(context.applicationContext)
//                        }
//                    })
                    .build()
            }
            return instance!!
        }

//        private fun fillInDb(context: Context) {
//            GlobalScope.launch {
//
//            }
//        }
    }
}
