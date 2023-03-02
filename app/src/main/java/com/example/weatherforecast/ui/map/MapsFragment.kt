package com.example.weatherforecast.ui.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.weatherforecast.R
import com.example.weatherforecast.model.Pojos.Settings
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val TAG = "MapsFragment"
class MapsFragment : Fragment(), OnMapReadyCallback{
 lateinit var getCurrentWeatherBtn:Button
 private var marker: Marker? = null
private lateinit var mapsViewModel:MapsViewModel
private var settings: Settings?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var v:View=inflater.inflate(R.layout.fragment_maps, container, false)
        getCurrentWeatherBtn=v.findViewById(R.id.btnGetCurrentWeather)
        mapsViewModel= ViewModelProvider(this,
            MapsViewModelFactory(requireContext().applicationContext)
        ).get(MapsViewModel::class.java)
        settings=mapsViewModel.getSettings()
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        val arg:MapsFragmentArgs by navArgs()
        if(arg.isSettings) {
            getCurrentWeatherBtn.text = "Save"
            getCurrentWeatherBtn.setOnClickListener {
                settings?.isMap = true
                settings?.lat = marker?.position!!.latitude
                settings?.lon = marker?.position!!.longitude
                Log.i(TAG, "onViewCreated: settings lat" + settings?.lat)
                Log.i(TAG, "onViewCreated: settings lon" + settings?.lon)
                mapsViewModel.saveSettings(settings)
                Navigation.findNavController(it).navigate(R.id.navigation_dashboard)

            }
        }
            if(arg.isFav)
            {
                getCurrentWeatherBtn.text="save data"
                getCurrentWeatherBtn.setOnClickListener {
                    lifecycleScope.launch{
                        mapsViewModel.insertFavorite(marker?.position!!.latitude,marker?.position!!.longitude)
                        Navigation.findNavController(it).navigate(R.id.favoritesFragment)
                    }

                }
            }
        }
    override fun onMapReady(googleMap: GoogleMap) {

        val sydney = LatLng(-34.0, 151.0)
       marker=googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.setOnMapClickListener {
         marker?.position=it
            marker?.title=it.toString()

        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        val apiKey = getString(R.string.APIKey)
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.PHOTO_METADATAS,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
        )
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                place.latLng?.let {
                  marker?.position=it
                    marker?.title=place.name
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 10f))
                }
            }

            override fun onError(status: Status) {
                Toast.makeText(requireContext(), status.toString(), Toast.LENGTH_SHORT).show()

            }
        })

        }

    }



