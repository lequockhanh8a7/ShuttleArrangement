package com.example.draftproject.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.example.draftproject.R
import com.example.draftproject.databinding.ActivityAddPassengerBinding
import com.example.draftproject.view.fragment.AddMapFragment
import com.google.android.material.snackbar.Snackbar

class AddPassenger : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddPassengerBinding.inflate (layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager.findFragmentByTag("MapFragment") as AddMapFragment
        binding.LocateButton.setOnClickListener(View.OnClickListener {
            val submitName = binding.PassengerInputText.text?.toString ()
            val submitPickup = binding.PickupInputText.text?.toString ()
            val submitDropoff = binding.DropoffInputText.text?.toString ()
            mapFragment.requestAddress (submitName, submitPickup, submitDropoff)
        })


        mapFragment.errorMessage.observe (this, Observer {
            if (it != null) {
                Snackbar.make(binding.root, it as CharSequence, Snackbar.LENGTH_LONG).show()
            }
        })
        mapFragment.passengerName.observe (this, Observer {
            binding.PassengerInputText.setText (it)
        })
        mapFragment.dropoffLocation.observe (this, Observer {
            binding.DropoffInputText.setText (it)
        })
        mapFragment.pickupLocation.observe (this, Observer {
            binding.PickupInputText.setText (it)
        })



    }
}