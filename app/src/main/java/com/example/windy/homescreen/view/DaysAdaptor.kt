package com.example.windy.homescreen.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.windy.databinding.DailyWeatherItemBinding
import com.example.windy.databinding.HourlyWeatherItemBinding
import com.example.windy.model.DailyWeatherData
import com.example.windy.model.HourlyWeatherData
import java.text.SimpleDateFormat
import java.util.*

class DaysDiffUtil : DiffUtil.ItemCallback<DailyWeatherData>(){
    override fun areItemsTheSame(oldItem: DailyWeatherData, newItem: DailyWeatherData): Boolean {
        return oldItem.weather[0].id == newItem.weather[0].id
    }

    override fun areContentsTheSame(oldItem: DailyWeatherData, newItem: DailyWeatherData): Boolean {
        return oldItem.weather[0].id == newItem.weather[0].id
    }

}

class DailyWeatherViewHolder(var holderBinding : DailyWeatherItemBinding): RecyclerView.ViewHolder(holderBinding.root)

class DailyWeatherAdaptor(private val onClick : (DailyWeatherData) -> Unit) : ListAdapter<DailyWeatherData, DailyWeatherViewHolder>(DaysDiffUtil()){
    lateinit var adaptorBinding : DailyWeatherItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyWeatherViewHolder {
        val inflater : LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        adaptorBinding = DailyWeatherItemBinding.inflate(inflater,parent,false)
        return DailyWeatherViewHolder(adaptorBinding)
    }

    override fun onBindViewHolder(holder: DailyWeatherViewHolder, position: Int) {
        val currentObj = getItem(position)
        val simpleDate = SimpleDateFormat("EEEE")
        val currentDate = simpleDate.format(Date(currentObj.dt * 1000L))
        holder.holderBinding.weatherDescriptionDay.text = currentObj.weather[0].description.uppercase()
        Glide.with(holder.itemView.context)
            .load("https://openweathermap.org/img/wn/"+currentObj.weather[0].icon+"@2x.png")
            .into(holder.holderBinding.dayImage)
        holder.holderBinding.dayName.text = currentDate
        holder.holderBinding.lowTemp.text = "L: "+currentObj.temp.min.toString()+"°"
        holder.holderBinding.highTemp.text = "H: "+currentObj.temp.max.toString()+"°"
    }
}