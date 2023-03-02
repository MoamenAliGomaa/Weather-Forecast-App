package com.example.weatherforecast.ui.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.databinding.FavoriteRowBinding
import com.example.weatherforecast.databinding.WeatherHourlyItemBinding
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Pojos.Current
import com.example.weatherforecast.model.Pojos.Settings
import com.example.weatherforecast.model.Pojos.Welcome
import com.example.weatherforecast.model.Utils


class FavoritesAdatpter(var welcomeList:List<Welcome>, var context: Context):
    RecyclerView.Adapter<FavoritesAdatpter.ViewHolder>(){
    lateinit var binding: FavoriteRowBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FavoriteRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var current=welcomeList[position]
        holder.binding.tvCityNameFavorite.text=Utils.getAddressEnglish(context,current.lat,current.lon)
        holder.binding.btnDelete.setOnClickListener {
            //todo delete from data base
        }
    }

    override fun getItemCount(): Int = welcomeList.size

    inner class ViewHolder(var binding: FavoriteRowBinding): RecyclerView.ViewHolder(binding.root)
}