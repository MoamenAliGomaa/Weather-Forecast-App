package com.example.weatherforecast.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.databinding.WeatherDayItemBinding
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Pojos.Daily
import com.example.weatherforecast.model.Utils


class DailyAdapter(var dailyList: List<Daily>, var context: Context) :
    RecyclerView.Adapter<DailyAdapter.ViewHolder>() {
    lateinit var binding: WeatherDayItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = WeatherDayItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var current = dailyList[position]

            Glide.with(context).load(Utils.getIconUrl(current.weather[0].icon))
                .into(holder.binding.iconTemp)
            holder.binding.tvDescrptionDaily.text = current.weather[0].description
            holder.binding.tvMaxTemp.text = current.temp.max.toString() + Constants.KELVIN
            holder.binding.tvMinTemp.text = current.temp.min.toString() + Constants.KELVIN
        if (position == 0) {
            holder.binding.tvDateDaily.text =""
            holder.binding.tvNameDay.text = "Tomorrow"
        }
            else {
            holder.binding.tvDateDaily.text = Utils.formatDate(current.dt)
            holder.binding.tvNameDay.text = Utils.formatday(current.dt)
        }

    }

    override fun getItemCount(): Int = dailyList.size

    inner class ViewHolder(var binding: WeatherDayItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}