package com.example.windy

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.example.windy.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    lateinit var settingsBinding: FragmentSettingsBinding
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        settingsBinding = FragmentSettingsBinding.inflate(inflater,container,false)
        return settingsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("MyPreferences",
            Context.MODE_PRIVATE
        )
        if(sharedPreferences.getString("Location","Using Map")=="Using GPS"){
            settingsBinding.usingGpsBtn.isChecked = true
        }else{
            settingsBinding.usingMapBtn.isChecked = true
        }
        if(sharedPreferences.getString("Language","en")=="en"){
            settingsBinding.englsihLang.isChecked = true
        }else{
            settingsBinding.arabicLang.isChecked = true
        }
        if(sharedPreferences.getString("Unit","metric")=="metric"){
            settingsBinding.metricUnit.isChecked = true
        }else{
            settingsBinding.imperialUnit.isChecked = true
        }
        if(sharedPreferences.getString("Alerts","Alert")=="Alert"){
            settingsBinding.alerts.isChecked = true
        }else{
            settingsBinding.notifications.isChecked = true
        }
        val editor = sharedPreferences.edit()

        settingsBinding.locationOptions.setOnCheckedChangeListener { group, checkedId ->
            val checkedButton = group.findViewById<RadioButton>(checkedId)
            when(checkedButton.text){
                "Using GPS"->{
                    editor.putString("Location","Using GPS")
                }
                "Using Map"->{
                    editor.putString("Location","Using Map")
                }
            }
        }
        settingsBinding.unitsOptions.setOnCheckedChangeListener { group, checkedId ->
            val checkedButton = group.findViewById<RadioButton>(checkedId)
            when(checkedButton.text){
                "Metric"->{
                    editor.putString("Unit","metric")
                }
                "Imperial"->{
                    editor.putString("Unit","imperial")
                }
            }
        }
        settingsBinding.langOptions.setOnCheckedChangeListener { group, checkedId ->
            val checkedButton = group.findViewById<RadioButton>(checkedId)
            when(checkedButton.text){
                "English"->{
                    editor.putString("Language","en")
                }
                "Arabic"->{
                    editor.putString("Language","ar")
                }
            }
        }
        settingsBinding.alertsOptions.setOnCheckedChangeListener { group, checkedId ->
            val checkedButton = group.findViewById<RadioButton>(checkedId)
            when(checkedButton.text){
                "Alerts"->{
                    editor.putString("Alerts","Alerts")
                }
                "Notifications"->{
                    editor.putString("Alerts","Notifications")
                }
            }
        }

        settingsBinding.saveSettings.setOnClickListener {
            editor.apply()
            val intent = activity!!.packageManager.getLaunchIntentForPackage(
                activity!!.packageName
            )
            intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent!!)
        }
    }
}