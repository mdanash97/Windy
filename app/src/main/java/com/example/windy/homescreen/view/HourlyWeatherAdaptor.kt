package com.example.windy.homescreen.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.windy.databinding.HourlyWeatherItemBinding
import com.example.windy.model.HourlyWeatherData
import java.text.SimpleDateFormat
import java.util.*

class HourlyWeatherDataDiffUtil : DiffUtil.ItemCallback<HourlyWeatherData>(){
    override fun areItemsTheSame(oldItem: HourlyWeatherData, newItem: HourlyWeatherData): Boolean {
        return oldItem.weather[0].id == newItem.weather[0].id
    }

    override fun areContentsTheSame(oldItem: HourlyWeatherData, newItem: HourlyWeatherData): Boolean {
        return oldItem.weather[0].id == newItem.weather[0].id
    }
}

class HourlyWeatherViewHolder(var holderBinding : HourlyWeatherItemBinding): RecyclerView.ViewHolder(holderBinding.root)

class  HourlyWeatherAdaptor(private val onClick : (HourlyWeatherData) -> Unit) : ListAdapter<HourlyWeatherData, HourlyWeatherViewHolder>(HourlyWeatherDataDiffUtil()){
    lateinit var adaptorBinding : HourlyWeatherItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        val inflater : LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        adaptorBinding = HourlyWeatherItemBinding.inflate(inflater,parent,false)
        return HourlyWeatherViewHolder(adaptorBinding)
    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        val currentObj = getItem(position)
        val simpleDate = SimpleDateFormat("KK:mm aaa")
        val currentDate = simpleDate.format(Date(currentObj.dt * 1000L))
        holder.holderBinding.time.text = currentDate
        Glide.with(holder.itemView.context)
            .load("https://openweathermap.org/img/wn/"+currentObj.weather[0].icon+"@2x.png")
            .into(holder.holderBinding.hourlyImg)
        holder.holderBinding.hourlyDescription.text = currentObj.weather[0].description.uppercase()
        holder.holderBinding.weatherHourly.text = currentObj.temp.toString()+"°"
    }
}