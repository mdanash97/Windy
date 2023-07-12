package com.example.windy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.windy.databinding.ActivityMainBinding
import com.example.windy.favoritescreen.view.FavoriteFragment
import com.example.windy.homescreen.view.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        navController = Navigation.findNavController(this,R.id.nav_host_fragment)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        NavigationUI.setupWithNavController(bottomNavigationView,navController,false)

        when(intent.getStringExtra("Saved")){
            "Save"->{
                navController.navigate(R.id.favoriteFragment)
            }
            "Alert"->{
                navController.navigate(R.id.alertsFragment)
            }
        }
    }
}