package com.example.windy.favoritescreen.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.windy.database.Location
import com.example.windy.databinding.FavoriteItemBinding

class LocationsDiffUtil : DiffUtil.ItemCallback<Location>(){
    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem.id == newItem.id
    }
}
class LocationViewHolder(var holderBinding : FavoriteItemBinding): RecyclerView.ViewHolder(holderBinding.root)

class LocationAdaptor(private val onSelect: OnSelect) : ListAdapter<Location, LocationViewHolder>(LocationsDiffUtil()){

    lateinit var adaptorBinding : FavoriteItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater : LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        adaptorBinding = FavoriteItemBinding.inflate(inflater,parent,false)
        return LocationViewHolder(adaptorBinding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val currentObj = getItem(position)
        holder.holderBinding.locationName.text = currentObj.name
        holder.holderBinding.removeButton.setOnClickListener{
            onSelect.onRemove(currentObj)
        }
        holder.holderBinding.locationItem.setOnClickListener {
            onSelect.onSelect(currentObj)
        }
    }

}