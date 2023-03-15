package com.example.weatherforecast.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.databinding.FragmentSettingsBinding
import com.example.weatherforecast.model.Network.RemoteDataSource
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Pojos.Settings
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.SharedPrefrences.SharedManger
import com.example.weatherforecast.model.database.LocalDataSource


private const val TAG = "SettingsFragment"

class SettingsFragment : Fragment() {


    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var settings: Settings? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        SharedManger.init(requireContext())
        var repository=Repository.getInstance(LocalDataSource(requireContext()),RemoteDataSource())
        val settingsViewModel =
            ViewModelProvider(this, SettingsViewModelFactory(repository)).get(
                SettingsViewModel::class.java
            )

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        settings = settingsViewModel.getSettings()
        Log.i(TAG, "onCreateView: " + settings)
        initUi()

        binding.buttonSave.setOnClickListener {
            savButtonCheck()
            Log.i(TAG, "onCreateView: " + settings)
            settingsViewModel.saveSettings(settings)
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
          //  Navigation.findNavController(it).navigate(R.id.navigation_home)

        }


        binding.radioButtonMap.setOnClickListener {
            val action=SettingsFragmentDirections.actionNavigationDashboardToMapsFragment(true,false,false)
            Navigation.findNavController(it).navigate(action)
        }
        return root
    }

    fun initUi() {
        if (settings?.isMap == false) {
            binding.radioButtonGps.isChecked = true
        }
        if (settings?.isMap == true) {
            binding.radioButtonMap.isChecked = true
        }

        if (settings?.unit == Constants.UNITS_DEFAULT) {
            binding.radioButtonKelvin.isChecked = true
        }
        if (settings?.unit == Constants.UNITS_FAHRENHEIT) {
            binding.radioButtonFehrihaite.isChecked = true
        }
        if (settings?.unit == Constants.UNITS_CELSIUS) {
            binding.radioButtonCelicus.isChecked = true
        }
        if (settings?.lang == Constants.LANG_EN) {
            binding.radioButtonEnglish.isChecked = true
        }
        if (settings?.lang == Constants.LANG_AR) {
            binding.radioButtonArabic.isChecked = true
        }
    }

    fun savButtonCheck() {
        if (binding.radioButtonGps.isChecked) {
            settings?.isMap = false
        }
        if (binding.radioButtonMap.isChecked) {
            settings?.isMap = true
        }
        if (binding.radioButtonArabic.isChecked) {

            settings?.lang = Constants.LANG_AR
        }
        if (binding.radioButtonEnglish.isChecked) {

            settings?.lang = Constants.LANG_EN
        }
        if (binding.radioButtonKelvin.isChecked) {
            settings?.unit = Constants.UNITS_DEFAULT
        }
        if (binding.radioButtonFehrihaite.isChecked) {
            settings?.unit = Constants.UNITS_FAHRENHEIT
        }
        if (binding.radioButtonCelicus.isChecked) {
            settings?.unit = Constants.UNITS_CELSIUS
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}