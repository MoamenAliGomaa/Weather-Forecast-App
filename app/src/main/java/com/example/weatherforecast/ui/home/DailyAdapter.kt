package com.example.weatherforecast.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.databinding.WeatherDayItemBinding
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Pojos.Daily
import com.example.weatherforecast.model.Pojos.Settings
import com.example.weatherforecast.model.Utils


class DailyAdapter(var dailyList: List<Daily>, var context: Context,var settings: Settings?) :
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
            if(settings?.unit==Constants.UNITS_DEFAULT) {
                if (settings?.lang==Constants.LANG_AR) {
                    holder.binding.tvMaxTemp.text =
                        Utils.englishNumberToArabicNumber(current.temp.max.toString()) + Constants.KELVIN
                    holder.binding.tvMinTemp.text =
                        Utils.englishNumberToArabicNumber(current.temp.min.toString()) + Constants.KELVIN
                }
                if (settings?.lang==Constants.LANG_EN) {
                    holder.binding.tvMaxTemp.text = current.temp.max.toString() + Constants.KELVIN
                    holder.binding.tvMinTemp.text = current.temp.min.toString() + Constants.KELVIN
                }
            }
        if(settings?.unit==Constants.UNITS_FAHRENHEIT) {
            if (settings?.lang==Constants.LANG_AR) {
                holder.binding.tvMaxTemp.text =
                    Utils.englishNumberToArabicNumber(current.temp.max.toString()) + Constants.FAHRENHEIT
                holder.binding.tvMinTemp.text =
                    Utils.englishNumberToArabicNumber(current.temp.min.toString()) + Constants.FAHRENHEIT
            }
            if (settings?.lang==Constants.LANG_EN) {
                holder.binding.tvMaxTemp.text = current.temp.max.toString() + Constants.FAHRENHEIT
                holder.binding.tvMinTemp.text = current.temp.min.toString() + Constants.FAHRENHEIT
            }
        }
        if(settings?.unit==Constants.UNITS_CELSIUS) {
            if (settings?.lang==Constants.LANG_AR) {
                holder.binding.tvMaxTemp.text =
                    Utils.englishNumberToArabicNumber(current.temp.max.toString()) + Constants.CELSIUS
                holder.binding.tvMinTemp.text =
                    Utils.englishNumberToArabicNumber(current.temp.min.toString()) + Constants.CELSIUS
            }
            if (settings?.lang==Constants.LANG_EN) {
                holder.binding.tvMaxTemp.text = current.temp.max.toString() + Constants.CELSIUS
                holder.binding.tvMinTemp.text = current.temp.min.toString() + Constants.CELSIUS
            }
        }
        if (position == 0) {
            if (settings?.lang==Constants.LANG_AR) {
                holder.binding.tvDateDaily.text = ""
                holder.binding.tvNameDay.text = "غدا"
            }
            if (settings?.lang==Constants.LANG_EN){
                holder.binding.tvDateDaily.text = ""
                holder.binding.tvNameDay.text = "Tomorrow"
            }
        }
            else {
             if (settings?.lang==Constants.LANG_EN) {
                 holder.binding.tvDateDaily.text = Utils.formatDate(current.dt)
                 holder.binding.tvNameDay.text = Utils.formatday(current.dt)
             }
            if (settings?.lang==Constants.LANG_AR){
                holder.binding.tvDateDaily.text = Utils.formatDateArabic(current.dt)
                holder.binding.tvNameDay.text = Utils.formatdayArabic(current.dt)
            }
        }

    }

    override fun getItemCount(): Int = dailyList.size

    inner class ViewHolder(var binding: WeatherDayItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}