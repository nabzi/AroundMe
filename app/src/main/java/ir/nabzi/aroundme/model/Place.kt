package ir.nabzi.aroundme.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "place")
@Parcelize
data class Place
(
        @PrimaryKey
        val id: String,
        val title: String,
        val subTitle: String,
        val imageUrl: String,
        val rating: Int,
        val location_lat: Double,
        val location_lng: Double
) : Parcelable
