package com.example.windy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity() ,OnMapReadyCallback{

    lateinit var sharedPreferences: SharedPreferences
    lateinit var setLocationButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment : SupportMapFragment =  supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        sharedPreferences = this.getSharedPreferences("MyPreferences",
            Context.MODE_PRIVATE
        )
        setLocationButton = findViewById(R.id.setLocationBtn)

        setLocationButton.setOnClickListener {
            val intent = Intent(this@MapActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        var googleMap = p0
        val editor = sharedPreferences.edit()
        var longitude = sharedPreferences.getString("LongitudeMap","0.0")!!.toDouble()
        var latitude = sharedPreferences.getString("LatitudeMap","0.0")!!.toDouble()
        var alex = LatLng(latitude,longitude)
        googleMap.addMarker(MarkerOptions().position(alex).title("Alexandria"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(alex, 8f))

        googleMap.setOnMapClickListener {
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(it).title(it.toString()))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
            editor.putString("LatitudeMap",it.latitude.toString())
            editor.putString("LongitudeMap",it.longitude.toString())
            editor.apply()
        }
    }
}