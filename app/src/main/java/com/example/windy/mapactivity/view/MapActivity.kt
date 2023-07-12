package com.example.windy.mapactivity.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.windy.MainActivity
import com.example.windy.R
import com.example.windy.database.AppDatabase
import com.example.windy.database.ConcreteLocalSource
import com.example.windy.database.Location
import com.example.windy.mapactivity.viewmodel.MapViewModel
import com.example.windy.mapactivity.viewmodel.MapViewModelFactory
import com.example.windy.model.Repository
import com.example.windy.network.WeatherClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.*

class MapActivity : AppCompatActivity() ,OnMapReadyCallback{

    lateinit var sharedPreferences: SharedPreferences
    lateinit var setLocationButton: Button
    lateinit var backButton : Button
    lateinit var autocompleteFragment : AutocompleteSupportFragment
    lateinit var googleMap:GoogleMap
    lateinit var mapViewModel: MapViewModel
    lateinit var mapViewModelFactory: MapViewModelFactory
    lateinit var geocoder: Geocoder

    var state = ""
    var latitude = 0.0
    var longitude = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        Places.initialize(baseContext,getString(R.string.myApiKey))
        autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autoComplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID,Place.Field.ADDRESS,Place.Field.LAT_LNG))
        val mapFragment =  supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        state = intent.getStringExtra("Saved Location").toString()

        mapViewModelFactory = MapViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(),
                ConcreteLocalSource(AppDatabase.getInstance(this).getLocationDAo(),AppDatabase.getInstance(this).getAlertsDAo())
            )
        )
        mapViewModel = ViewModelProvider(this,mapViewModelFactory)[MapViewModel::class.java]
        geocoder = Geocoder(this, Locale.getDefault())

        sharedPreferences = this.getSharedPreferences("MyPreferences",
            Context.MODE_PRIVATE
        )
        setLocationButton = findViewById(R.id.setLocationBtn)
        backButton = findViewById(R.id.backBtn)
        backButton.visibility = View.GONE

        if(state=="Saved"){
            setLocationButton.text = "Save Location"
            backButton.visibility = View.VISIBLE
        }
        val intent = Intent(this@MapActivity, MainActivity::class.java)
        setLocationButton.setOnClickListener {
            if(state=="Saved") {
                intent.putExtra("Saved","Save")
                var addresses = geocoder.getFromLocation(latitude, longitude, 4)
                var city = addresses!![0].adminArea
                println(addresses)
                println(latitude+longitude)
                var location = Location(name = city, longitude = longitude, latitude = latitude)
                mapViewModel.insertFav(location)
            }
            startActivity(intent)
            finish()
        }
        backButton.setOnClickListener {
            if(state=="Saved") {
                intent.putExtra("Saved","Save")
            }
            startActivity(intent)
            finish()
        }
        autocompleteFragment.setOnPlaceSelectedListener(object :PlaceSelectionListener{
            override fun onError(p0: Status) {
                Toast.makeText(this@MapActivity,"Error in Searching",Toast.LENGTH_LONG).show()
            }

            override fun onPlaceSelected(p0: Place) {
                val latLng = p0.latLng
                googleMap.clear()
                googleMap.addMarker(MarkerOptions().position(latLng).title(latLng.toString()))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            }

        })
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        val editor = sharedPreferences.edit()
        var longitudeMap = sharedPreferences.getString("LongitudeMap","0.0")!!.toDouble()
        var latitudeMap = sharedPreferences.getString("LatitudeMap","0.0")!!.toDouble()
        var alex = LatLng(latitudeMap,longitudeMap)
        googleMap.addMarker(MarkerOptions().position(alex).title("Your Location"))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(alex, 8f))

        googleMap.setOnMapClickListener {
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(it).title(it.toString()))
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(it))
            if(state=="Saved"){
                latitude = it.latitude
                longitude = it.longitude
            }else{
                editor.putString("LatitudeMap",it.latitude.toString())
                editor.putString("LongitudeMap",it.longitude.toString())
                editor.apply()
            }
        }
    }
}