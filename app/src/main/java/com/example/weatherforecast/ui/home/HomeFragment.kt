package com.example.weatherforecast.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Utils
import com.google.android.gms.location.*
import java.time.LocalDate
import java.util.*

private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel:HomeViewModel
    private lateinit var fusedClient : FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
     homeViewModel= ViewModelProvider(this,HomeViewModelFactory(requireContext().applicationContext)).get(HomeViewModel::class.java)
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLastLocation()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root



        homeViewModel.welcomeCurrentWeather.observe(viewLifecycleOwner) {

            Glide.with(requireContext()).load(Utils.getIconUrl(it.current.weather[0].icon))
                .apply(
                    RequestOptions().override(400, 300).placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                ).into(binding.iconTemp)

            binding.tvTempeatur.text=it.current.temp.toString()+Constants.KELVIN
            binding.tvHumidity.text="Humidity : "+it.current.humidity.toString()+"%"
            binding.tvWeather.text=it.current.weather[0].description
            binding.tvDay.text=Utils.formatday(it.current.dt)
            binding.tvWindspeed.text="Wind Speed : "+it.current.wind_speed.toString()+Constants.WINDSPEED
            binding.tvFeelsLike.text="Real feel : "+it.current.feels_like.toString()+Constants.KELVIN
            binding.tvPressure.text="Pressure : "+it.current.pressure.toString()+Constants.MBAR
            binding.tvSunRise.text="Sun rise : "+Utils.formatTime(it.current.sunrise!!)
            binding.tvSunSet.text="Sun set : "+Utils.formatTime(it.current.sunset!!)
            binding.tvDate.text=Utils.formatDate(it.current.dt)
            Log.i(TAG, "onCreateView: "+it.hourly)
            binding.rvWeatherHourly.adapter=HourlyAdapter(it.hourly,requireContext())
                binding.rvWeatherHourly.apply {
                adapter = binding.rvWeatherHourly.adapter
                layoutManager = LinearLayoutManager(requireContext())
                    .apply { orientation = RecyclerView.HORIZONTAL }
            }
            var listDaily=it.daily as MutableList
            listDaily.removeAt(0)
            binding.rvWeatherDaily.adapter=DailyAdapter(listDaily,requireContext())
            binding.rvWeatherDaily.apply {
                adapter = binding.rvWeatherDaily.adapter
                layoutManager = LinearLayoutManager(requireContext())
                    .apply { orientation = RecyclerView.VERTICAL }
            }


        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun checkPermission():Boolean{
        return ActivityCompat.checkSelfPermission(requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )== PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )== PackageManager.PERMISSION_GRANTED
    }
    fun requestPermission(){
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ),
            Constants.PERMISSIN_ID)
    }
    fun requestNewLocation(mLocationCallBack: LocationCallback) {
        val mLocationRequest= LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(0)
        fusedClient= LocationServices.getFusedLocationProviderClient(requireActivity())
        if(checkPermission())
            fusedClient.requestLocationUpdates(mLocationRequest,mLocationCallBack, Looper.myLooper())
    }

    fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().baseContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== Constants.PERMISSIN_ID)
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
                getLastLocation()

        }
    }
    @SuppressLint("Missing Permission")
    private fun getLastLocation() {
        if(checkPermission())
        {
            if(isLocationEnabled())
            {
                requestNewLocation(mLocationCallBack)
            }
            else{
                Toast.makeText(requireContext(),"Turn on your location", Toast.LENGTH_SHORT).show()
                val intent= Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }


        }
        else
            requestPermission()

    }
    private val mLocationCallBack: LocationCallback =object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            val mlastLocation: Location? =p0?.lastLocation
            homeViewModel.getCurrentWeather(lon = mlastLocation?.longitude.toString(), lat = mlastLocation?.latitude.toString())
            val geocoder= Geocoder(requireContext())
            val address =geocoder.getFromLocation(mlastLocation?.latitude!!,mlastLocation?.longitude!!,1)
            binding.toolbarLayout.tvCityName.text=address?.get(0)?.countryName.toString()+" , "+address?.get(0)?.adminArea
            fusedClient.removeLocationUpdates(this)
        }

    }
}