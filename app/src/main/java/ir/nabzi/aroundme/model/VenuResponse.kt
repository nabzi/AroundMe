package ir.nabzi.aroundme.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class LocationResponse(val address : String, val lat:Double, val lng: Double): Parcelable
/**
 * "meta": {
"code": 200
"requestId": "609402d51d1a5131ee585294"
}
"response": {
"venues": [
{
"id": "4e1bc7896284102ec19762b9"
"name": "Azadi Square (میدان آزادی)"
"contact": {
}
"location": {
"address": "Azadi Sq., Azadi St."
"lat": 35.6996688757595
"lng": 51.337952613830566
"labeledLatLngs": [
"0": {
"label": "display"
"lat": 35.6996688757595
"lng": 51.337952613830566
}
]
"distance": 52
"cc": "IR"
"city": "Tehran"
"state": "Tehran"
"country": "Iran"
"formattedAddress": [
"0": "Azadi Sq., Azadi St."
"1": "Tehran, Tehran"
]
}
"categories": [
"0": {
"id": "4bf58dd8d48988d164941735"
"name": "Plaza"
"pluralName": "Plazas"
"shortName": "Plaza"
"icon": {
"prefix": "https://ss3.4sqi.net/img/categories_v2/parks_outdoors/plaza_"
"suffix": ".png"
}
"primary": true
}
]
"stats": {
"tipCount": 120
"usersCount": 13409
"checkinsCount": 33008
}
"hereNow": {
"count": 1
"summary": "One other person is here"
"groups": [
"0": {
"type": "others"
"name": "Other people here"
"count": 1
"items": [
]
}
]
}
"referralId": "v-1620313316"
"venueChains": [
]
"hasPerk": false
}
}
 *
 * */