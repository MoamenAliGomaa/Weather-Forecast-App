package com.example.weatherforecast

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.model.Network.RemoteDataSource
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.SharedPrefrences.SharedManger
import com.example.weatherforecast.model.Utils
import com.example.weatherforecast.model.database.LocalDataSource
import com.example.weatherforecast.ui.home.HomeViewModel
import com.example.weatherforecast.ui.home.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var homeViewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener {
           override fun onDestinationChanged(
                navController: NavController,
                navDestination: NavDestination,
                bundle: Bundle?
            ) {
                if (navDestination.id == R.id.navigation_dashboard||navDestination.id==R.id.mapsFragment) {
                    navView.visibility = View.GONE
                } else {
                    navView.visibility = View.VISIBLE
                }
            }
        })





    }
    override fun attachBaseContext(newBase: Context?) {

        SharedManger.init(newBase!!)
        if(SharedManger.getSettings()?.lang.equals(Constants.LANG_AR))
        {
            val lang_code = "ar" //load it from SharedPref
            val context: Context = Utils.changeLang(newBase!!, lang_code)!!

        }
        else
        {
            val lang_code = "en" //load it from SharedPref
            val context: Context = Utils.changeLang(newBase!!, lang_code)!!

        }

        super.attachBaseContext(newBase)
    }






}