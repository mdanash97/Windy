package com.example.windy.homescreen.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.windy.database.AppDatabase
import com.example.windy.database.ConcreteLocalSource
import com.example.windy.databinding.FragmentHomeBinding
import com.example.windy.homescreen.viewmodel.HomeViewModel
import com.example.windy.homescreen.viewmodel.HomeViewModelFactory
import com.example.windy.model.Repository
import com.example.windy.network.NetworkResult
import com.example.windy.network.WeatherClient
import com.google.android.gms.location.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

const val PERMISSION_ID = 44

class HomeFragment : Fragment() {

    lateinit var homeBinding: FragmentHomeBinding
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var hourlyWeatherAdaptor: HourlyWeatherAdaptor
    lateinit var dailyWeatherAdaptor: DailyWeatherAdaptor
    lateinit var sharedPreferences: SharedPreferences
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    lateinit var geocoder: Geocoder

    var city = ""
    var units = arrayOf("","")
    var longitude = 0.0
    var latitude = 0.0
    var lang = "en"
    var unit = "metric"
    var saved:Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater,container,false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("MyPreferences", MODE_PRIVATE)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        homeViewModelFactory = HomeViewModelFactory(Repository.getInstance(WeatherClient.getInstance(),
            ConcreteLocalSource(AppDatabase.getInstance(requireContext()).getLocationDAo(),AppDatabase.getInstance(requireContext()).getAlertsDAo())
        ))
        homeViewModel = ViewModelProvider(this,homeViewModelFactory)[HomeViewModel::class.java]

        hourlyWeatherAdaptor = HourlyWeatherAdaptor {
            println("new weather")
        }
        dailyWeatherAdaptor = DailyWeatherAdaptor {
            println("new day")
        }

        unit = sharedPreferences.getString("Unit","metric").toString()
        lang = sharedPreferences.getString("Language","en").toString()

        var location = HomeFragmentArgs.fromBundle(arguments!!).newLocations
        if (location != null) {
                homeViewModel.getWeatherData(location.latitude,location.longitude,unit,lang)
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 4)
            city = addresses!![0].adminArea
            saved = false
        }else{
            if(sharedPreferences.getString("Location","Using Map")=="Using Map"){
                longitude = sharedPreferences.getString("LongitudeMap","0.0")!!.toDouble()
                latitude = sharedPreferences.getString("LatitudeMap","0.0")!!.toDouble()
                homeViewModel.getWeatherData(latitude,longitude,unit,lang)
                val addresses = geocoder.getFromLocation(latitude, longitude, 4)
                city = addresses!![0].adminArea
            }
        }

        when(sharedPreferences.getString("Unit","metric")){
            "metric"->{
                units[0] = "meter/sec"
                units[1] = "C"
            }
            "imperial"->{
                units[0] = "miles/hour"
                units[1] = "K"
            }
        }


        lifecycleScope.launch {
            homeViewModel.weatherData.collect{result ->
                when(result){
                    NetworkResult.Loading -> {
                        homeBinding.homeView.visibility = View.GONE
                        homeBinding.progressBar.visibility = View.VISIBLE
                        homeBinding.loadingtext.visibility = View.VISIBLE
                        homeBinding.errorImg.visibility = View.GONE
                        homeBinding.errorText.visibility = View.GONE
                    }
                    is NetworkResult.Error -> {
                        homeBinding.homeView.visibility = View.GONE
                        homeBinding.progressBar.visibility = View.GONE
                        homeBinding.loadingtext.visibility = View.GONE
                        homeBinding.errorImg.visibility = View.VISIBLE
                        homeBinding.errorText.visibility = View.VISIBLE
                        Toast.makeText(requireContext(),result.message,Toast.LENGTH_LONG)
                    }
                    is NetworkResult.Success -> {
                        homeBinding.homeView.visibility = View.VISIBLE
                        homeBinding.progressBar.visibility = View.GONE
                        homeBinding.loadingtext.visibility = View.GONE
                        homeBinding.errorImg.visibility = View.GONE
                        homeBinding.errorText.visibility = View.GONE
                        setData(result)
                        homeBinding.cityName.text = city
                    }
                }
            }
        }

        homeBinding.hourlyRV.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = hourlyWeatherAdaptor
            hasFixedSize()
        }
        homeBinding.daysRV.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            adapter = dailyWeatherAdaptor
            hasFixedSize()
        }

    }


    private fun setData(result: NetworkResult.Success){
        val simpleDate = SimpleDateFormat("EEEE dd/MM/yyyy")
        val currentDate = simpleDate.format(Date(result.data.current.dt * 1000L))
        Glide.with(requireContext())
            .load("https://openweathermap.org/img/wn/"+result.data.current.weather[0].icon+"@2x.png").apply(
                RequestOptions().override(600, 800))
            .into(homeBinding.weatherImg)
        homeBinding.weatherDescription.text = result.data.current.weather[0].description.uppercase()
        hourlyWeatherAdaptor.submitList(result.data.hourly.subList(fromIndex = 0, toIndex = 24))
        homeBinding.weatherDescription.text = result.data.current.weather[0].description.uppercase()
        homeBinding.temperatureText.text = result.data.current.temp.toString()+"°"+units[1]
        homeBinding.highAndLowTemp.text = "L: "+result.data.daily[0].temp.min.toString()+"°   H: "+result.data.daily[0].temp.max.toString()+"°"
        homeBinding.dayTextView.text = currentDate
        homeBinding.humidityLevel.text = result.data.current.humidity.toString()+"%"
        homeBinding.windSpeed.text = result.data.current.wind_speed.toString()+" "+units[0]
        if(result.data.daily[0].rain.toString()!="null"){
            homeBinding.rainLevel.text = result.data.daily[0].rain.toString()+" mm/h"
        }else{
            homeBinding.rainLevel.text = "0.0 mm/h"
        }
        homeBinding.feelsLike.text = result.data.current.feels_like.toString()+"°"+units[1]
        homeBinding.windDirection.text = result.data.current.wind_deg.toString()+" Degrees"
        homeBinding.clouds.text = result.data.current.clouds.toString()+"%"
        homeBinding.pressure.text = result.data.current.pressure.toString()+" hPa"

        dailyWeatherAdaptor.submitList(result.data.daily)

    }


    private val mLocationCallback: LocationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val mLastLocation: Location? = locationResult.lastLocation
            if(saved){
                if (sharedPreferences.getString("Location", "Using Map") == "Using GPS") {
                    if (mLastLocation != null) {
                        homeViewModel.getWeatherData(mLastLocation.latitude, mLastLocation.longitude,unit,lang)
                        val addresses =
                            geocoder.getFromLocation(mLastLocation.latitude, mLastLocation.longitude, 4)
                        city = addresses!![0].adminArea
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getLastLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if(checkPermissions()){
            if(isLocationEnabled()){
                requestNewLocationData()

            }else{
                Toast.makeText(requireContext(),"Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else{
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        var result = false
        if((ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )== PackageManager.PERMISSION_GRANTED)&&
            (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )== PackageManager.PERMISSION_GRANTED)){
            result = true
        }
        return result
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(0)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest,mLocationCallback, Looper.myLooper()
        )
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

}