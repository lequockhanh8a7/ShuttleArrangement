package com.example.draftproject.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.draftproject.R
import com.example.draftproject.utils.googleapi.GeocodingResponse
import com.example.draftproject.viewmodel.AddPassengerViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions


class AddMapFragment () : Fragment() {
    private lateinit var _mainMap : GoogleMap
    private lateinit var _fragmentViewModel : AddPassengerViewModel
    private lateinit var _appContext : Context

    private val _passengerName = MutableLiveData <String?> ()
    private val _pickupLocation = MutableLiveData <String?> ()
    private val _dropoffLocation = MutableLiveData <String?> ()
    private val _errorMessage = MutableLiveData <String?> ()

    val passengerName : LiveData <String?> get () = _passengerName
    val pickupLocation : LiveData <String?> get () = _pickupLocation
    val dropoffLocation : LiveData <String?> get () = _dropoffLocation
    val errorMessage : LiveData <String?> get () = _errorMessage




    private val callback = OnMapReadyCallback { googleMap ->
        _mainMap = googleMap
        _mainMap.uiSettings.isZoomControlsEnabled = true
    }

    private fun changeAttribute (response : Pair <GeocodingResponse?, GeocodingResponse?>) {

        if (response.first != null && response.second != null) {
            if (response.first!!.error != null) {
                _errorMessage.value = response.first!!.error
            } else if (response.second!!.error != null) {
                _errorMessage.value = response.first!!.error
            } else {
                _pickupLocation.value =  response.first!!.address
                _dropoffLocation.value = response.second!!.address
                _mainMap.clear ()
                _mainMap.addMarker (MarkerOptions()
                        .position (response.first!!.location!!)
                        .title (response.first!!.address)
                        .icon (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                _mainMap.addMarker (MarkerOptions()
                        .position (response.second!!.location!!)
                        .title (response.second!!.address)
                        .icon (BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                _mainMap.moveCamera (CameraUpdateFactory.newLatLngZoom (response.first!!.location, 10f))
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _appContext = context
        _fragmentViewModel = AddPassengerViewModel (_appContext)
        _fragmentViewModel.responseHolder.observe(this, Observer {
            changeAttribute (it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mymap) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }





    fun requestAddress (name : String?, pickupAddress : String?, dropoffAddress : String? ) {
        _passengerName.value = name
        if (name == null || pickupAddress == null || dropoffAddress == null) {
            _errorMessage.value = "At least one of three fields is empty. Fill them in"
        } else {
            _fragmentViewModel.locateAddress(pickupAddress, dropoffAddress)
        }

    }

}