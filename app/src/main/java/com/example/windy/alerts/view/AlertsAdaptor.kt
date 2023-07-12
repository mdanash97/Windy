package com.example.windy.alerts.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.windy.database.Alerts
import com.example.windy.databinding.AlertsItemBinding
import com.example.windy.favoritescreen.view.OnSelect
import com.example.windy.model.DailyWeatherData
import java.text.SimpleDateFormat
import java.util.*


class AlertsDiffUtil : DiffUtil.ItemCallback<Alerts>(){
    override fun areItemsTheSame(oldItem: Alerts, newItem: Alerts): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Alerts, newItem: Alerts): Boolean {
        return oldItem.id == newItem.id
    }
}

class AlertsViewHolder(var holderBinding : AlertsItemBinding): RecyclerView.ViewHolder(holderBinding.root)

class AlertsAdaptor(private val onClick : (Alerts) -> Unit) : ListAdapter<Alerts, AlertsViewHolder>(
    AlertsDiffUtil()
){

    lateinit var adaptorBinding : AlertsItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertsViewHolder {
        val inflater : LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        adaptorBinding = AlertsItemBinding.inflate(inflater,parent,false)
        return AlertsViewHolder(adaptorBinding)
    }

    override fun onBindViewHolder(holder: AlertsViewHolder, position: Int) {
        val currentObj = getItem(position)
        val date = Date(currentObj.time)
        val simpleDate = SimpleDateFormat("dd/MM/yyyy KK:mm:ss aaa")
        val currentDate = simpleDate.format(date)
        holder.holderBinding.alertTime.text = currentDate
        holder.holderBinding.alertTitle.text = currentObj.title
        holder.holderBinding.removeButton.setOnClickListener{
            onClick(currentObj)
        }

    }

}
