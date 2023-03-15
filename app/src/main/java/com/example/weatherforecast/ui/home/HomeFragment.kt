package com.example.weatherforecast.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.FloatRange
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kotlinproducts.view.API
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.model.Network.RemoteDataSource
import com.example.weatherforecast.model.Pojos.ApiState
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.SharedPrefrences.SharedManger
import com.example.weatherforecast.model.Utils
import com.example.weatherforecast.model.database.LocalDataSource
import com.example.weatherforecast.model.database.WeatherDataBse
import com.example.weatherforecast.ui.LoadingFragment.LoadingDialog
import com.google.android.gms.location.*
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private lateinit var homeViewModel:HomeViewModel
    private lateinit var fusedClient : FusedLocationProviderClient
    var progressDialog: DialogFragment =    LoadingDialog()
    lateinit var navBar:NavigationBarView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): RelativeLayout?
    {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: RelativeLayout? = binding?.root

        SharedManger.init(requireContext())
        var repository= Repository.getInstance(LocalDataSource(requireContext()), RemoteDataSource())
        homeViewModel= ViewModelProvider(this,HomeViewModelFactory(repository)).get(HomeViewModel::class.java)
        fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val arg: HomeFragmentArgs by navArgs()
        if(arg.isComingFav)
        {
            navBar=requireActivity().findViewById(R.id.nav_view)
            navBar.visibility=View.GONE
            var favObj=arg.welcomFavObj
            binding?.toolbarLayout?.tvCityName?.text=Utils.getAddressEnglish(requireContext(),favObj?.lat,favObj?.lon)
            binding?.tvTempeatur?.text = favObj?.current?.temp.toString() + Constants.KELVIN
            binding?.tvFeelsLike?.text = "Real feel : " + favObj?.current?.feels_like.toString() + Constants.KELVIN
            binding?.iconTemp?.let {
                Glide.with(requireContext()).load(Utils.getIconUrl(favObj?.current?.weather?.get(0)?.icon.toString()))
                    .apply(
                        RequestOptions().override(400, 300)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_foreground)
                    ).into(it)
            }
            binding?.tvHumidity?.text = "Humidity : " + favObj?.current?.humidity.toString() + "%"
            binding?.tvWeather?.text = favObj?.current?.weather?.get(0)?.description.toString()
            binding?.tvDay?.text = Utils.formatday(favObj?.current?.dt!!)
            binding?.tvWindspeed?.text =
                "Wind Speed : " + favObj.current.wind_speed.toString() + Constants.WINDSPEED
            binding?.tvPressure?.text =
                "Pressure : " + favObj.current.pressure.toString() + Constants.MBAR
            binding?.tvSunRise?.text = "Sun rise : " + Utils.formatTime(favObj.current.sunrise!!)
            binding?.tvSunSet?.text = "Sun set : " + Utils.formatTime(favObj.current.sunset!!)
            binding?.tvDate?.text = Utils.formatDate(favObj.current.dt)
            binding?.rvWeatherHourly?.adapter =
                HourlyAdapter(favObj.hourly, requireContext(), homeViewModel.getSettings())
            binding?.rvWeatherHourly?.apply {
                adapter = binding?.rvWeatherHourly?.adapter
                layoutManager = LinearLayoutManager(requireContext())
                    .apply { orientation = RecyclerView.HORIZONTAL }
            }
            var listDaily =favObj.daily as MutableList
            listDaily.removeAt(0)
            binding?.rvWeatherDaily?.adapter =
                DailyAdapter(listDaily, requireContext(), homeViewModel.getSettings())
            binding?.rvWeatherDaily?.apply {
                adapter = binding?.rvWeatherDaily?.adapter
                layoutManager = LinearLayoutManager(requireContext())
                    .apply { orientation = RecyclerView.VERTICAL }
            }


        }
        else
        {
            if(Utils.isOnline(requireContext())){

                Snackbar.make(activity?.window?.decorView!!.rootView, "Online", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(android.R.color.holo_green_light))
                    .show()
                lifecycleScope.launch {
                    if(homeViewModel.getSettings()?.isMap==false)
                        getLastLocation()
                    if(homeViewModel.getSettings()?.isMap == true)
                        getLocationMap()



                    homeViewModel.welcomeCurrentWeather.collectLatest { result ->
                        when (result) {
                            is ApiState.Loading -> {
                                progressDialog.show(childFragmentManager, LoadingDialog.TAG)
                            }
                            is ApiState.Success -> {
                                progressDialog.dismiss()
                                result.data.isFavorite=false
                                homeViewModel.insertOrUpdateCurrent(result.data)

                                binding?.iconTemp?.let {
                                    Glide.with(requireContext()).load(Utils.getIconUrl(result.data.current.weather[0].icon.toString()))
                                        .apply(
                                            RequestOptions().override(400, 300)
                                                .placeholder(R.drawable.ic_launcher_background)
                                                .error(R.drawable.ic_launcher_foreground)
                                        ).into(it)
                                }

                                if (homeViewModel.getSettings()?.lang == Constants.LANG_EN) {
                                    binding?.tvNextDays?.text = "Next Days"
                                    binding?.tvNextHour?.text = "Next Hours"
                                    binding?.toolbarLayout?.tvCityName?.text=Utils.getAddressEnglish(requireContext(),result?.data?.lat,result?.data?.lon)

                                    if (homeViewModel.getSettings()?.unit == Constants.UNITS_DEFAULT) {
                                        binding?.tvTempeatur?.text = result.data.current.temp.toString() + Constants.KELVIN
                                        binding?.tvFeelsLike?.text =
                                            "Real feel : " + result.data.current.feels_like.toString() + Constants.KELVIN
                                    }
                                    if (homeViewModel.getSettings()?.unit == Constants.UNITS_CELSIUS) {
                                        binding?.tvTempeatur?.text  = result.data.current.temp.toString() + Constants.CELSIUS
                                        binding?.tvFeelsLike?.text   =
                                            "Real feel : " + result.data.current.feels_like.toString() + Constants.CELSIUS
                                    }
                                    if (homeViewModel.getSettings()?.unit == Constants.UNITS_FAHRENHEIT) {
                                        binding?.tvTempeatur?.text = result.data.current.temp.toString() + Constants.FAHRENHEIT
                                        binding?.tvFeelsLike?.text =
                                            "Real feel : " + result.data.current.feels_like.toString() + Constants.FAHRENHEIT
                                    }
                                    binding?.tvHumidity?.text = "Humidity : " + result.data.current.humidity.toString() + "%"
                                    binding?.tvWeather?.text = result.data.current.weather[0].description.toString()
                                    binding?.tvDay?.text = Utils.formatday(result.data.current.dt)
                                    binding?.tvWindspeed?.text =
                                        "Wind Speed : " + result.data.current.wind_speed.toString() + Constants.WINDSPEED
                                    binding?.tvPressure?.text =
                                        "Pressure : " + result.data.current.pressure.toString() + Constants.MBAR
                                    binding?.tvSunRise?.text = "Sun rise : " + Utils.formatTime(result.data.current.sunrise!!)
                                    binding?.tvSunSet?.text = "Sun set : " + Utils.formatTime(result.data.current.sunset!!)
                                    binding?.tvDate?.text = Utils.formatDate(result.data.current.dt)
                                }
                                if (homeViewModel.getSettings()?.lang == Constants.LANG_AR) {
                                    binding?.tvNextDays?.text = "الايام القادمة"
                                    binding?.tvNextHour?.text = "الساعات القادمة"
                                    binding?.toolbarLayout?.tvCityName?.text=Utils.getAddressArabic(requireContext(),result?.data?.lat!!,result?.data?.lon!!)

                                    if (homeViewModel.getSettings()?.unit == Constants.UNITS_DEFAULT) {
                                        binding?.tvTempeatur?.text =
                                            Utils.englishNumberToArabicNumber(result.data.current.temp.toString()) + Constants.KELVIN
                                        binding?.tvFeelsLike?.text =
                                            "الشعور الحقيقي : " + Utils.englishNumberToArabicNumber(result.data.current.feels_like.toString()) + Constants.KELVIN
                                    }
                                    if (homeViewModel.getSettings()?.unit == Constants.UNITS_CELSIUS) {
                                        binding?.tvTempeatur?.text =
                                            Utils.englishNumberToArabicNumber(result.data.current.temp.toString()) + Constants.CELSIUS
                                        binding?.tvFeelsLike?.text =
                                            "الشعور الحقيقي  : " + Utils.englishNumberToArabicNumber(result.data.current.feels_like.toString()) + Constants.CELSIUS
                                    }
                                    if (homeViewModel.getSettings()?.unit == Constants.UNITS_FAHRENHEIT) {
                                        binding?.tvTempeatur?.text =
                                            Utils.englishNumberToArabicNumber(result.data.current.temp.toString()) + Constants.FAHRENHEIT
                                        binding?.tvFeelsLike?.text =
                                            "الشعور الحقيقي : " + Utils.englishNumberToArabicNumber(result.data.current.feels_like.toString()) + Constants.FAHRENHEIT
                                    }
                                    binding?.tvHumidity?.text =
                                        "الرطوبة : " + Utils.englishNumberToArabicNumber(result.data.current.humidity.toString()) + "%"
                                    binding?.tvWeather?.text = result.data.current.weather[0].description.toString()
                                    binding?.tvDay?.text = Utils.formatdayArabic(result.data.current.dt)
                                    binding?.tvWindspeed?.text =
                                        "سرعة الرياح : " + Utils.englishNumberToArabicNumber(result.data.current.wind_speed.toString()) + Constants.WINDSPEEDARABIC
                                    binding?.tvPressure?.text =
                                        "الضغط : " + Utils.englishNumberToArabicNumber(result.data.current.pressure.toString()) + Constants.MBARARABIC
                                    binding?.tvSunRise?.text =
                                        "الشروق : " + Utils.formatTimeArabic(result.data.current.sunrise!!)
                                    binding?.tvSunSet?.text =
                                        "الغروب : " + Utils.formatTimeArabic(result.data.current.sunset!!)
                                    binding?.tvDate?.text = Utils.formatDateArabic(result.data.current.dt)

                                }
                                Log.i(TAG, "onCreateView: print call " + result.data.hourly)
                                binding?.rvWeatherHourly?.adapter =
                                    HourlyAdapter(result.data.hourly, requireContext(), homeViewModel.getSettings())
                                binding?.rvWeatherHourly?.apply {
                                    adapter = binding?.rvWeatherHourly?.adapter
                                    layoutManager = LinearLayoutManager(requireContext())
                                        .apply { orientation = RecyclerView.HORIZONTAL }
                                }
                                var listDaily = result.data.daily as MutableList
                                listDaily?.removeAt(0)
                                binding?.rvWeatherDaily?.adapter =
                                    DailyAdapter(listDaily, requireContext(), homeViewModel.getSettings())
                                binding?.rvWeatherDaily?.apply {
                                    adapter = binding?.rvWeatherDaily?.adapter
                                    layoutManager = LinearLayoutManager(requireContext())
                                        .apply { orientation = RecyclerView.VERTICAL }
                                }


                            }


                            is ApiState.Fail -> {
                                progressDialog.dismiss()

                            }


                        }


                    }
                }
            }
            else
            {
                Snackbar.make(activity?.window?.decorView!!.rootView, "Offline", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(resources.getColor(android.R.color.holo_red_light))
                    .show()
                homeViewModel.getCurrentWeatherLocal()
                lifecycleScope.launch {
                    homeViewModel.welcomeCurrentWeatherLocal.collectLatest { result->
                        when (result) {
                            is ApiState.Loading ->{
                                progressDialog.show(childFragmentManager, LoadingDialog.TAG)
                            }
                            is ApiState.Fail ->{
                                progressDialog.dismiss()

                            }
                            is ApiState.Success ->{
                                progressDialog.dismiss()
                                binding?.toolbarLayout?.tvCityName?.text=Utils.getAddressEnglish(requireContext(),result?.data?.lat,result?.data?.lon)
                                binding?.tvTempeatur?.text = result?.data?.current?.temp.toString() + Constants.KELVIN
                                binding?.tvFeelsLike?.text = "Real feel : " + result?.data?.current?.feels_like.toString() + Constants.KELVIN
                                binding?.iconTemp?.let {
                                    Glide.with(requireContext()).load(Utils.getIconUrl(result?.data?.current?.weather?.get(0)?.icon.toString()))
                                        .apply(
                                            RequestOptions().override(400, 300)
                                                .placeholder(R.drawable.ic_launcher_background)
                                                .error(R.drawable.ic_launcher_foreground)
                                        ).into(it)
                                }
                                binding?.tvHumidity?.text = "Humidity : " + result?.data?.current?.humidity.toString() + "%"
                                binding?.tvWeather?.text =result?.data?.current?.weather?.get(0)?.description.toString()
                                binding?.tvDay?.text = Utils.formatday(result?.data?.current?.dt!!)
                                binding?.tvWindspeed?.text =
                                    "Wind Speed : " + result.data.current.wind_speed.toString() + Constants.WINDSPEED
                                binding?.tvPressure?.text =
                                    "Pressure : " +  result.data.current.pressure.toString() + Constants.MBAR
                                binding?.tvSunRise?.text = "Sun rise : " + Utils.formatTime( result.data.current.sunrise!!)
                                binding?.tvSunSet?.text = "Sun set : " + Utils.formatTime( result.data.current.sunset!!)
                                binding?.tvDate?.text = Utils.formatDate( result.data.current.dt)
                                binding?.rvWeatherHourly?.adapter =
                                    HourlyAdapter( result.data.hourly, requireContext(), homeViewModel.getSettings())
                                binding?.rvWeatherHourly?.apply {
                                    adapter = binding?.rvWeatherHourly?.adapter
                                    layoutManager = LinearLayoutManager(requireContext())
                                        .apply { orientation = RecyclerView.HORIZONTAL }
                                }
                                var listDaily = result.data.daily as MutableList
                                listDaily?.removeAt(0)
                                binding?.rvWeatherDaily?.adapter =
                                    DailyAdapter(listDaily, requireContext(), homeViewModel.getSettings())
                                binding?.rvWeatherDaily?.apply {
                                    adapter = binding?.rvWeatherDaily?.adapter
                                    layoutManager = LinearLayoutManager(requireContext())
                                        .apply { orientation = RecyclerView.VERTICAL }
                                }


                            }



                        }



                    }
                }


            }

        }


        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.toolbarLayout?.btnSettings?.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.navigation_dashboard)
        }


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
    private fun getLocationMap(){
        @FloatRange(from = -90.0, to = 90.0)
        var lat =homeViewModel.getSettings()?.lat
        @FloatRange(from = -180.0, to = 180.0)
        var lon=homeViewModel.getSettings()?.lon
        if(homeViewModel.getSettings()?.lang==Constants.LANG_EN) {


            if(homeViewModel.getSettings()!!.unit==Constants.UNITS_CELSIUS)
            {
                homeViewModel.getCurrentWeather(
                    lon = lon.toString(),
                    lat = lat.toString(),
                    units = Constants.UNITS_CELSIUS
                )
            }
            if(homeViewModel.getSettings()!!.unit==Constants.UNITS_FAHRENHEIT)
            {
                homeViewModel.getCurrentWeather(
                    lon = lon.toString(),
                    lat = lat.toString(),
                    units = Constants.UNITS_FAHRENHEIT
                )
            }
            if (homeViewModel.getSettings()!!.unit==Constants.UNITS_DEFAULT){
                homeViewModel.getCurrentWeather(
                    lon = lon.toString(),
                    lat = lat.toString()
                )
            }

        }
        if(homeViewModel.getSettings()?.lang==Constants.LANG_AR) {

            if(homeViewModel.getSettings()!!.unit==Constants.UNITS_CELSIUS)
            {
                homeViewModel.getCurrentWeather(
                    lon = lon.toString(),
                    lat = lat.toString(),
                    units = Constants.UNITS_CELSIUS,
                    lang = Constants.LANG_AR
                )
            }
            if(homeViewModel.getSettings()!!.unit==Constants.UNITS_FAHRENHEIT)
            {
                homeViewModel.getCurrentWeather(
                    lon = lon.toString(),
                    lat = lat.toString(),
                    units = Constants.UNITS_FAHRENHEIT,
                    lang = Constants.LANG_AR
                )
            }
            if(homeViewModel.getSettings()!!.unit==Constants.UNITS_DEFAULT){
                homeViewModel.getCurrentWeather(
                    lon = lon.toString(),
                    lat = lat.toString(),
                    lang = Constants.LANG_AR
                )
            }

        }

    }
    private val mLocationCallBack:LocationCallback=object :LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            val mlastLocation: Location? =p0?.lastLocation
            if(homeViewModel.getSettings()?.lang==Constants.LANG_EN) {
                if(homeViewModel.getSettings()!!.unit==Constants.UNITS_CELSIUS)
                {
                    homeViewModel.getCurrentWeather(
                        lon = mlastLocation?.longitude.toString(),
                        lat = mlastLocation?.latitude.toString(),
                        units = Constants.UNITS_CELSIUS
                    )
                }
                if(homeViewModel.getSettings()!!.unit==Constants.UNITS_FAHRENHEIT)
                {
                    homeViewModel.getCurrentWeather(
                        lon = mlastLocation?.longitude.toString(),
                        lat = mlastLocation?.latitude.toString(),
                        units = Constants.UNITS_FAHRENHEIT
                    )
                }
            if (homeViewModel.getSettings()!!.unit==Constants.UNITS_DEFAULT){
                homeViewModel.getCurrentWeather(
                    lon = mlastLocation?.longitude.toString(),
                    lat = mlastLocation?.latitude.toString()
                )
               }
            }
            if(homeViewModel.getSettings()?.lang==Constants.LANG_AR) {


                if(homeViewModel.getSettings()!!.unit==Constants.UNITS_CELSIUS)
                {
                    homeViewModel.getCurrentWeather(
                        lon = mlastLocation?.longitude.toString(),
                        lat = mlastLocation?.latitude.toString(),
                        units = Constants.UNITS_CELSIUS,
                        lang = Constants.LANG_AR
                    )
                }
                if(homeViewModel.getSettings()!!.unit==Constants.UNITS_FAHRENHEIT)
                {
                    homeViewModel.getCurrentWeather(
                        lon = mlastLocation?.longitude.toString(),
                        lat = mlastLocation?.latitude.toString(),
                        units = Constants.UNITS_FAHRENHEIT,
                        lang = Constants.LANG_AR
                    )
                }
                if(homeViewModel.getSettings()!!.unit==Constants.UNITS_DEFAULT){
                    homeViewModel.getCurrentWeather(
                        lon = mlastLocation?.longitude.toString(),
                        lat = mlastLocation?.latitude.toString(),
                        lang = Constants.LANG_AR
                    )
                }

            }
            fusedClient.removeLocationUpdates(this)
        }
    }


}