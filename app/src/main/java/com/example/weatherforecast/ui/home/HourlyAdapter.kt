package com.example.weatherforecast.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.WeatherHourlyItemBinding
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Pojos.Current
import com.example.weatherforecast.model.Pojos.Settings
import com.example.weatherforecast.model.Utils


class HourlyAdapter(var hourlyList:List<Current>, var context: Context,var settings: Settings?):
    RecyclerView.Adapter<HourlyAdapter.ViewHolder>(){
    lateinit var binding: WeatherHourlyItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = WeatherHourlyItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var current=hourlyList[position]

        Glide.with(context).load(Utils.getIconUrl(current.weather[0].icon))
            .into(holder.binding.iconTemp)
        if (settings?.lang==Constants.LANG_EN) {
            holder.binding.tvHourDay.text = Utils.formatTime(current.dt)
            if (settings?.unit == Constants.UNITS_DEFAULT)
                holder.binding.tvTemp.text = current.temp.toString() + Constants.KELVIN
            if (settings?.unit == Constants.UNITS_CELSIUS)
                holder.binding.tvTemp.text = current.temp.toString() + Constants.CELSIUS
            if (settings?.unit == Constants.UNITS_FAHRENHEIT)
                holder.binding.tvTemp.text = current.temp.toString() + Constants.FAHRENHEIT
            holder.binding.tvHumidityHour.text = current.humidity.toString() + "%"
            holder.binding.tvWindSpeedHour.text =
                current.wind_speed.toString() + Constants.WINDSPEED
            holder.binding.tvDescription.text = current.weather[0].description
        }
        if (settings?.lang==Constants.LANG_AR)
        {
            holder.binding.tvHourDay.text = Utils.formatTimeArabic(current.dt)
            if (settings?.unit == Constants.UNITS_DEFAULT)
                holder.binding.tvTemp.text =Utils.englishNumberToArabicNumber(current.temp.toString())  + Constants.KELVIN
            if (settings?.unit == Constants.UNITS_CELSIUS)
                holder.binding.tvTemp.text = Utils.englishNumberToArabicNumber(current.temp.toString()) + Constants.CELSIUS
            if (settings?.unit == Constants.UNITS_FAHRENHEIT)
                holder.binding.tvTemp.text = Utils.englishNumberToArabicNumber(current.temp.toString()) + Constants.FAHRENHEIT
            holder.binding.tvHumidityHour.text = Utils.englishNumberToArabicNumber(current.humidity.toString()) + "%"
            holder.binding.tvWindSpeedHour.text =
              Utils.englishNumberToArabicNumber(current.wind_speed.toString() )  + Constants.WINDSPEEDARABIC
            holder.binding.tvDescription.text = current.weather[0].description

        }
    }

    override fun getItemCount(): Int = hourlyList.size

    inner class ViewHolder(var binding: WeatherHourlyItemBinding): RecyclerView.ViewHolder(binding.root)
}