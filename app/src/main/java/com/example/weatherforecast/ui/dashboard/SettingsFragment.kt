package com.example.weatherforecast.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentSettingsBinding
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Pojos.Settings



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
        val settingsViewModel =
            ViewModelProvider(this, SettingsViewModelFactory(requireContext())).get(
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
            Navigation.findNavController(it).navigate(R.id.navigation_home)
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