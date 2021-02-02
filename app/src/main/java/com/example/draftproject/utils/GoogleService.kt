package com.example.draftproject.utils

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject


abstract class GoogleService <T> (context : Context) {
    //private section
    private val cache = StoredCache.getInstance(context)

    // protected section
    protected val googleAPIURL = "https://maps.googleapis.com/maps/api"
    protected val format = "json"
    protected val APIkey = context.packageManager
            .getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            .metaData.getString("com.google.android.geo.API_KEY")?: ""

    protected abstract fun parseResponse (response : JSONObject?) : T




    protected fun requestURL (url : String) {
        val jsonObjectRequest = JsonObjectRequest (Request.Method.GET, url, null,
                Response.Listener { requestResponse ->
                    _response.value = parseResponse (requestResponse)

                },
                Response.ErrorListener { error ->
                    _response.value = parseResponse (null)
                }
        )
        cache.addToRequestQueue(jsonObjectRequest)
    }

    // public section
    private val _response = MutableLiveData <T> ()
    val response : LiveData<T> get () = _response


}