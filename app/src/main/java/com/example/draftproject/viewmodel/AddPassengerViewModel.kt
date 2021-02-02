package com.example.draftproject.viewmodel

import android.content.Context
import android.util.Log

import androidx.lifecycle.*
import com.example.draftproject.utils.googleapi.GeocodingResponse
import com.example.draftproject.utils.googleapi.GoogleGeocoding



class AddPassengerViewModel (context: Context) : ViewModel () {
    private val _responseHolder = MediatorLiveData <Pair<GeocodingResponse?, GeocodingResponse?>> ()
    private val _pickupRequest = GoogleGeocoding (context)
    private val _dropoffRequest = GoogleGeocoding (context)

    val responseHolder : LiveData <Pair<GeocodingResponse?, GeocodingResponse?>> get () = _responseHolder

    init {

        _responseHolder.apply {
            var pickupResponse : GeocodingResponse? = null
            var dropoffResponse : GeocodingResponse? = null

            fun update () {
                if (pickupResponse != null && dropoffResponse != null) {
                    this.value = Pair (pickupResponse, dropoffResponse)
                }
            }
            addSource (_pickupRequest.response) {
                pickupResponse = it
                update ()
            }
            addSource (_dropoffRequest.response) {
                dropoffResponse = it
                update ()
            }

        }

    }



    fun locateAddress (pickupAddress : String, dropoffAddress: String ) {
        _pickupRequest.locateAddress (pickupAddress)
        _dropoffRequest.locateAddress (dropoffAddress)
    }


}