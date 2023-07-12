package com.example.windy.alerts.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windy.alerts.viewmodel.AlertViewModel
import com.example.windy.alerts.viewmodel.AlertsViewModelFactory
import com.example.windy.database.AppDatabase
import com.example.windy.database.ConcreteLocalSource
import com.example.windy.database.DatabaseResult
import com.example.windy.databinding.FragmentAlertsBinding
import com.example.windy.mapactivity.viewmodel.MapViewModelFactory
import com.example.windy.model.Repository
import com.example.windy.network.WeatherClient
import kotlinx.coroutines.launch

class AlertsFragment : Fragment() {

    lateinit var alertsBinding: FragmentAlertsBinding
    lateinit var alertViewModelFactory: AlertsViewModelFactory
    lateinit var alertViewModel: AlertViewModel
    lateinit var alertsAdaptor: AlertsAdaptor
    lateinit var alertScheduler: AlertScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        alertsBinding = FragmentAlertsBinding.inflate(inflater,container,false)
        return alertsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        alertScheduler = AlertScheduler(requireContext())

        alertViewModelFactory = AlertsViewModelFactory(
            Repository.getInstance(
            WeatherClient.getInstance(),
            ConcreteLocalSource(
                AppDatabase.getInstance(requireContext()).getLocationDAo(),
                AppDatabase.getInstance(requireContext()).getAlertsDAo())
        ))
        alertViewModel = ViewModelProvider(this,alertViewModelFactory)[AlertViewModel::class.java]
        alertsBinding.addAlertBtn.setOnClickListener {
            val intent = Intent(requireContext(), AlertActivity::class.java)
            startActivity(intent)
        }

        alertsAdaptor = AlertsAdaptor {
            alertViewModel.deleteAlert(it)
            alertScheduler.cancel(it)
        }

        alertsBinding.alersRV.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = alertsAdaptor
            hasFixedSize()
        }

        lifecycleScope.launch {
            alertViewModel.alerts.collect { result ->
                when (result) {
                    is DatabaseResult.Loading -> {

                    }
                    is DatabaseResult.Success -> {

                    }
                    is DatabaseResult.Error -> {

                    }
                    is DatabaseResult.SuccessAlerts -> {
                        alertsAdaptor.submitList(result.data)
                    }
                }
            }
        }
    }

}