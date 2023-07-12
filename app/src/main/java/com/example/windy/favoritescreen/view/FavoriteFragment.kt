package com.example.windy.favoritescreen.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windy.mapactivity.view.MapActivity
import com.example.windy.database.AppDatabase
import com.example.windy.database.ConcreteLocalSource
import com.example.windy.database.DatabaseResult
import com.example.windy.database.Location
import com.example.windy.databinding.FragmentFavoriteBinding
import com.example.windy.favoritescreen.viewmodel.FavoriteViewModel
import com.example.windy.favoritescreen.viewmodel.FavoriteViewModelFactory
import com.example.windy.model.Repository
import com.example.windy.network.WeatherClient
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() ,OnSelect{

    lateinit var favoriteBinding: FragmentFavoriteBinding
    lateinit var favoriteViewModel: FavoriteViewModel
    lateinit var favoriteViewModelFactory: FavoriteViewModelFactory
    lateinit var locationAdaptor: LocationAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        favoriteBinding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return favoriteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteViewModelFactory = FavoriteViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(),
            ConcreteLocalSource(AppDatabase.getInstance(requireContext()).getLocationDAo(),AppDatabase.getInstance(requireContext()).getAlertsDAo())
            )
        )
        favoriteViewModel = ViewModelProvider(this,favoriteViewModelFactory)[FavoriteViewModel::class.java]

        favoriteBinding.addLocationBtn.setOnClickListener {
            val intent = Intent(requireActivity(), MapActivity::class.java)
            intent.putExtra("Saved Location","Saved")
            startActivity(intent)
        }

        locationAdaptor = LocationAdaptor(this)

        favoriteBinding.favoriteRV.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            adapter = locationAdaptor
            hasFixedSize()
        }

        lifecycleScope.launch {
            favoriteViewModel.locations.collect{result ->
                when(result){
                    is DatabaseResult.Loading ->{
                        favoriteBinding.favoriteRV.visibility = View.GONE
                        favoriteBinding.loadingBar.visibility = View.VISIBLE
                        favoriteBinding.emptyImg.visibility = View.GONE
                        favoriteBinding.emptyList.visibility = View.GONE
                    }
                    is DatabaseResult.Success ->{
                        if(result.data.isEmpty()){
                            favoriteBinding.favoriteRV.visibility = View.GONE
                            favoriteBinding.loadingBar.visibility = View.GONE
                            favoriteBinding.emptyImg.visibility = View.VISIBLE
                            favoriteBinding.emptyList.visibility = View.VISIBLE
                        }else{
                            favoriteBinding.favoriteRV.visibility = View.VISIBLE
                            favoriteBinding.loadingBar.visibility = View.GONE
                            favoriteBinding.emptyImg.visibility = View.GONE
                            favoriteBinding.emptyList.visibility = View.GONE
                            locationAdaptor.submitList(result.data)
                        }
                    }
                    is DatabaseResult.Error ->{
                        favoriteBinding.favoriteRV.visibility = View.GONE
                        favoriteBinding.loadingBar.visibility = View.GONE
                        favoriteBinding.emptyImg.visibility = View.VISIBLE
                        favoriteBinding.emptyList.visibility = View.VISIBLE
                        favoriteBinding.emptyList.text = "Something Went Wrong.."
                    }
                    else -> {

                    }
                }
            }
        }

    }

    override fun onSelect(location: Location) {
        var action = FavoriteFragmentDirections.actionFavoriteFragmentToHomeFragment(location)
        findNavController().navigate(action)
    }

    override fun onRemove(location: Location) {
        favoriteViewModel.deleteLocation(location)
    }
}