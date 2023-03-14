package com.example.weatherforecast

import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.model.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


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



//        val config = resources.configuration
//        val lang = "fa" // your language code
//        val locale = Locale(lang)
//        Locale.setDefault(locale)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
//            config.setLocale(locale)
//        else
//            config.locale = locale
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//            createConfigurationContext(config)
//        resources.updateConfiguration(config, resources.displayMetrics)


    }



}