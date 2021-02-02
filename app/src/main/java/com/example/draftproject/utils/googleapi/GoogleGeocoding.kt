package com.example.draftproject.utils.googleapi

import android.content.Context
import android.util.Log
import com.example.draftproject.utils.GoogleService
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class GeocodingResponse (
        val error :String?,
        val address : String?,
        val location : LatLng?
)


class GoogleGeocoding (context: Context): GoogleService <GeocodingResponse> (context)  {


    override fun parseResponse(response: JSONObject?): GeocodingResponse {
        if (response == null) {
            return GeocodingResponse ("Could not connect the Map service. Please turn on your WiFi or data",  null, null)
        } else if (response.has ("error_message")) {
            return GeocodingResponse ("Unsuccessful request. Status = ${response.get ("status")}", null, null)
        }
        val result = response.getJSONArray("results").getJSONObject(0)
        val formattedAddress = result.getString ("formatted_address")
        val location = result.getJSONObject ("geometry").getJSONObject ("location")
        val latitude = location.getDouble ("lat")
        val longtitude = location.getDouble ("lng")
        return GeocodingResponse(null, formattedAddress, LatLng(latitude, longtitude))

    }

    fun locateAddress (address : String) {
        val url = "$googleAPIURL/geocode/$format?address=$address&key=$APIkey"
        requestURL (url)
    }


}