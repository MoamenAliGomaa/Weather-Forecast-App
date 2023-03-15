package com.example.weatherforecast.ui.favorites

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproducts.view.API
import com.example.weatherforecast.databinding.FavoriteRowBinding
import com.example.weatherforecast.model.IRepository
import com.example.weatherforecast.model.Network.RemoteDataSource
import com.example.weatherforecast.model.Pojos.Constants
import com.example.weatherforecast.model.Pojos.Welcome
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.model.SharedPrefrences.SharedManger
import com.example.weatherforecast.model.Utils
import com.example.weatherforecast.model.database.LocalDataSource
import com.example.weatherforecast.model.database.WeatherDataBse

private const val TAG = "FavoritesAdatpter"
class FavoritesAdatpter(var welcomeList: List<Welcome>?, var context: Context, val onClick:(Welcome)->Unit,val onClickShow:(Welcome)->Unit):
    RecyclerView.Adapter<FavoritesAdatpter.ViewHolder>(){
    lateinit var binding: FavoriteRowBinding
    lateinit var repository: IRepository
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FavoriteRowBinding.inflate(inflater, parent, false)
        SharedManger.init(context)
       repository=Repository.getInstance(LocalDataSource(context), RemoteDataSource())
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: "+welcomeList?.size)
        var current= welcomeList?.get(position)
        holder.binding.tvCityNameFav.text=Utils.getAddressEnglish(context, current?.lat,
            current?.lon
        )
        holder.binding.btnDeleteFav.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete Location")
                .setMessage("Do you really want to delete this location ?")
                .setIcon(R.drawable.ic_dialog_alert)
                .setPositiveButton(
                    R.string.yes,
                    DialogInterface.OnClickListener { dialog, whichButton ->
                        onClick(current!!)
                        Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show()

                    })
                .setNegativeButton(R.string.no, null).show()
        }
        holder.binding.rvFavoritesItem.setOnClickListener {
            onClickShow(current!!)
        }
        holder.binding.tvDescriptionFavorite.text= current?.current?.weather?.get(0)?.description ?: "no descreption"
        if(repository.getSettings()?.unit.equals(Constants.UNITS_DEFAULT))
        holder.binding.tvWeatherFavorite.text=current?.current?.temp.toString()+ Constants.KELVIN
        if(repository.getSettings()?.unit.equals(Constants.UNITS_CELSIUS))
            holder.binding.tvWeatherFavorite.text=current?.current?.temp.toString()+ Constants.CELSIUS
            if(repository.getSettings()?.unit.equals(Constants.UNITS_FAHRENHEIT))
                holder.binding.tvWeatherFavorite.text=current?.current?.temp.toString()+ Constants.FAHRENHEIT
    }

    override fun getItemCount(): Int = welcomeList?.size!!
    inner class ViewHolder(var binding: FavoriteRowBinding): RecyclerView.ViewHolder(binding.root)
}