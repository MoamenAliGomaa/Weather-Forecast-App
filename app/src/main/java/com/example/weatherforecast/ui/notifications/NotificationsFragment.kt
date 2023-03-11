package com.example.weatherforecast.ui.notifications


import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.annotation.RequiresApi

import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentNotificationsBinding
import java.util.*


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val notificationsViewModel =
            ViewModelProvider(this,NotificationsViewModelFactory(requireContext())).get(NotificationsViewModel::class.java)
        canDrawOverApps()

        binding.btnAddToAlerts.setOnClickListener{
            AlertDialogFragment().show(
                childFragmentManager, AlertDialogFragment.TAG)
        }



        return root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun canDrawOverApps(){
        if (!Settings.canDrawOverlays(requireContext())) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + activity?.packageName)
            )
            startActivityForResult(intent, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}