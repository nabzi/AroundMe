package ir.nabzi.aroundme.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.net.Inet4Address

data class NearbySearchResponse(
        val summary         : NearbySearchSummaryResponse,
        val results         : List<PointResponse>
)
data class NearbySearchSummaryResponse(
        val queryType       : String,
        val queryTime       : Int,
        val numResults      : Int,
        val offset          : Int,
        val totalResults    : Int,
)
/**
 * summary":{"queryType":"NEARBY","queryTime":26,"numResults":10,"offset":0,"totalResults":95,"fuzzyLevel":1,"geoBias":{"lat":35.702,"lon":51.3380464}},"results"
 * */

data class PointResponse(
    val id          : String,
    val score       : Double,
    val dist        : Double,
    val poi         : PoiResponse,
    val address     : AddressResponse,
    val position    : LocationResponse
)

data class PoiResponse(
        val name            : String ,
        val phone           : String,
        val url             : String?,
        val classifications : List<ClassificationsResponse>
)

data class ClassificationsResponse(
        val code : String
)
/**
 * "classifications": [
{
"code": "SHOP",
"names": [
{
"nameLocale": "en-US",
"name": "shop"
}
]
}
]
 * */
data class AddressResponse(
        val streetName : String
)
/**
 * "address": {
"streetNumber": "25",
"streetName": "North 2nd Street",
"municipalitySubdivision": "Downtown San Jose",
"municipality": "San Jose",
"countrySecondarySubdivision": "Santa Clara",
"countrySubdivision": "CA",
"countrySubdivisionName": "California",
"postalCode": "95113",
"extendedPostalCode": "95113-1205",
"countryCode": "US",
"country": "United States",
"countryCodeISO3": "USA",
"freeformAddress": "25 North 2nd Street, San Jose, CA 95113",
"localName": "San Jose"
},
 * */

data class LocationResponse(
        val lat     : Double,
        val lon     : Double)

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