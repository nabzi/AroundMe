package ir.nabzi.aroundme.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class PointResponse(
    val id : String,
    val score: Double,
    val dist: Double,
    val poi : PoiResponse,
)

data class PoiResponse(
        val name : String ,
        val position : LocationResponse,
        val url : String?
)

@Parcelize
data class LocationResponse(val lat:Double, val lon: Double): Parcelable

/**
 * {
"type": "POI",
"id": "g6JpZKc4MDgwMjY0oWOjSVJOoXanVW5pZmllZA==",
"score": 99.3223953247,
"dist": 677.6025945625089,
"info": "search:geonames:8080264",
"poi": {
"name": "Pārk-e Almehdī",
"categorySet": [
{
"id": 9362008
}
],
"categories": [
"park"
],
"classifications": [
{
"code": "PARK_RECREATION_AREA",
"names": [
{
"nameLocale": "en-US",
"name": "park"
}
]
}
]
},
"address": {
"municipality": "Tehran",
"countrySubdivision": "Tehran",
"countryCode": "IR",
"country": "Iran",
"countryCodeISO3": "IRN",
"freeformAddress": "Tehrān, Tehran",
"localName": "Tehrān"
},
"position": {
"lat": 35.69443,
"lon": 51.34059
},
"viewport": {
"topLeftPoint": {
"lat": 35.69533,
"lon": 51.33948
},
"btmRightPoint": {
"lat": 35.69353,
"lon": 51.3417
}
}
}
 *
 * */