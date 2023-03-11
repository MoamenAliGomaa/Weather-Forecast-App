package com.example.weatherforecast.ui.favorites

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.databinding.FavoriteRowBinding
import com.example.weatherforecast.model.Pojos.Welcome
import com.example.weatherforecast.model.Utils

private const val TAG = "FavoritesAdatpter"
class FavoritesAdatpter(var welcomeList: List<Welcome>?, var context: Context, val onClick:(Welcome)->Unit):
    RecyclerView.Adapter<FavoritesAdatpter.ViewHolder>(){
    lateinit var binding: FavoriteRowBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FavoriteRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: "+welcomeList?.size)
        var current= welcomeList?.get(position)
        holder.binding.tvCityNameFavorite.text=Utils.getAddressEnglish(context, current?.lat,
            current?.lon
        )
        holder.binding.btnDelete.setOnClickListener {
Toast.makeText(context,"clicke",Toast.LENGTH_SHORT).show()
                onClick(current!!)

        }
    }

    override fun getItemCount(): Int = welcomeList?.size!!
    inner class ViewHolder(var binding: FavoriteRowBinding): RecyclerView.ViewHolder(binding.root)
}