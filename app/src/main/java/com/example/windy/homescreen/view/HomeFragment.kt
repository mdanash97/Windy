package com.example.windy.homescreen.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    lateinit var homeBinding: FragmentHomeBinding
    lateinit var homeViewModel: HomeViewModel
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var hourlyWeatherAdaptor: HourlyWeatherAdaptor
    lateinit var dailyWeatherAdaptor: DailyWeatherAdaptor

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

        homeViewModelFactory = HomeViewModelFactory(Repository.getInstance(WeatherClient.getInstance(),
            ConcreteLocalSource(AppDatabase.getInstance(requireContext()).getLocationDAo())
        ))
        homeViewModel = ViewModelProvider(this,homeViewModelFactory)[HomeViewModel::class.java]

        hourlyWeatherAdaptor = HourlyWeatherAdaptor {
            println("new weather")
        }
        dailyWeatherAdaptor = DailyWeatherAdaptor {
            println("new day")
        }

        homeViewModel.setLocation(31.2001,29.9187)

        lifecycleScope.launch {
            homeViewModel.weatherData.collect{result ->
                when(result){
                    NetworkResult.Loading -> println("loading")
                    is NetworkResult.Error -> println("Error")
                    is NetworkResult.Success -> {
                        setData(result)
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
        homeBinding.temperatureText.text = result.data.current.temp.toString()+"°"
        homeBinding.highAndLowTemp.text = "L: "+result.data.daily[0].temp.min.toString()+"°   H: "+result.data.daily[0].temp.max.toString()+"°"
        homeBinding.dayTextView.text = currentDate
        homeBinding.humidityLevel.text = result.data.current.humidity.toString()+"%"
        homeBinding.windSpeed.text = result.data.current.wind_speed.toString()
        if(result.data.daily[0].rain.toString()!="null"){
            homeBinding.rainLevel.text = result.data.daily[0].rain.toString()+" mm/h"
        }else{
            homeBinding.rainLevel.text = "0.0 mm/h"
        }
        homeBinding.feelsLike.text = result.data.current.feels_like.toString()+"°"
        homeBinding.windDirection.text = result.data.current.wind_deg.toString()+" Degrees"
        homeBinding.clouds.text = result.data.current.clouds.toString()+"%"
        homeBinding.pressure.text = result.data.current.pressure.toString()+" hPa"

        dailyWeatherAdaptor.submitList(result.data.daily)

    }



}