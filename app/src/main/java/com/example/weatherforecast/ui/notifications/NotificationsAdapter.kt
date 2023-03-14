package com.example.weatherforecast.ui.notifications


import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.databinding.AlarmRowBinding
import com.example.weatherforecast.model.Pojos.Alert
import com.example.weatherforecast.model.Utils


class NotificationsAdapter(var alertList: List<Alert>?, var context: Context, val onClick:(Alert)->Unit) :
    RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {
    lateinit var binding: AlarmRowBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = AlarmRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var current = alertList?.get(position)

        holder.binding.tvStartTime.text=Utils.formatTimeAlert(
            current?.startTime ?: 0
        )
        if(Utils.isDaily(current?.startTime!!, current?.endTime!!))
        {
            holder.binding.tvEndTime.text="Daily | Ended on "+Utils.formatTimeAlert(current?.endTime!!)+"  "+Utils.formatDateAlert(current.endTime)

        }
        else{
            holder.binding.tvEndTime.text="Once | Ended on "+Utils.formatTimeAlert(current?.endTime!!)
        }

        Log.e("onBindViewHolder", "onBindViewHolder: start date "+Utils.formatDateAlert(current?.startTime ?: 0))
            holder.binding.tvAlarmCityName.text= current?.cityName ?:" "
        if(Utils.isOnline(context))
        {
            holder.binding.btnDeleteAlarm.isEnabled=true
            holder.binding.btnDeleteAlarm.setOnClickListener{

                AlertDialog.Builder(context)
                    .setTitle("Delete Alarm")
                    .setMessage("Do you really want to delete alarm ?")
                    .setIcon(R.drawable.ic_dialog_alert)
                    .setPositiveButton(
                        R.string.yes,
                        DialogInterface.OnClickListener { dialog, whichButton ->
                            onClick(current!!)

                        })
                    .setNegativeButton(R.string.no, null).show()
            }
        }
        else
        {
            holder.binding.btnDeleteAlarm.isEnabled=false
        }





    }

    override fun getItemCount(): Int = alertList?.size!!

    inner class ViewHolder(var binding: AlarmRowBinding) :
        RecyclerView.ViewHolder(binding.root)
}