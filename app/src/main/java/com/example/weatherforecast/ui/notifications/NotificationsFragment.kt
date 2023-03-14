package com.example.weatherforecast.ui.notifications


import android.app.AlarmManager
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.example.weatherforecast.databinding.FragmentNotificationsBinding
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Pojos.LocalDataStateAlerts
import com.example.weatherforecast.model.Utils
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    lateinit var progressDialog:ProgressDialog
    private val binding get() = _binding!!


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        canDrawOverApps()
        val notificationsViewModel =
            ViewModelProvider(this,NotificationsViewModelFactory(requireContext())).get(NotificationsViewModel::class.java)
        notificationsViewModel.getAlerts()
        progressDialog= Utils.progressDialog(requireContext())
        binding.btnAddToAlerts.setOnClickListener{

            AlertDialogFragment().show(
                childFragmentManager, AlertDialogFragment.TAG)
        }
        if (Utils.isOnline(requireContext()))
        {
            binding.btnAddToAlerts.visibility=View.VISIBLE

        }
        else
        {   binding.btnAddToAlerts.isEnabled=false
            binding.btnAddToAlerts.visibility=View.GONE

        }

        lifecycleScope.launch {
            notificationsViewModel.alertFlow.collectLatest {
                when(it)
                {
                    is LocalDataStateAlerts.Loading->{


                    }
                    is LocalDataStateAlerts.Fail->{

                        Toast.makeText(requireContext(),"Failed to fetch local data", Toast.LENGTH_SHORT).show()

                    }
                    is LocalDataStateAlerts.Success->{

                        if(it.data?.isEmpty() == true)
                        {

                            binding.noAlarmHolder.visibility=View.VISIBLE
                            binding.rvAlerts.visibility=View.GONE

                        }
                        else{
                            binding.rvAlerts.visibility=View.VISIBLE
                            binding.noAlarmHolder.visibility=View.GONE

                            binding.rvAlerts.apply {

                                adapter = NotificationsAdapter(it.data,requireContext()){
                                    Log.e("TAG", "onCreateView: alert info for adapter " +it)

                                    Utils.canelAlarm(requireContext(),it.toString(),it.startTime.toInt())
                                    notificationsViewModel.deleteAlert(it)
                                    WorkManager.getInstance(context.applicationContext).cancelAllWorkByTag(it.startTime.toString())
                                    Toast.makeText(requireContext(),"Your alarm been deleted", Toast.LENGTH_SHORT).show()
                                }
                                layoutManager = LinearLayoutManager(requireContext())
                                    .apply { orientation = RecyclerView.VERTICAL }
                            }

                        }





                    }


                }

            }



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