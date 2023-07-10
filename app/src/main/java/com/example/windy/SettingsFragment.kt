package com.example.windy

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
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
        val editor = sharedPreferences.edit()
        settingsBinding.locationOptions.setOnCheckedChangeListener { group, checkedId ->
            val checkedButton = group.findViewById<RadioButton>(checkedId)
            when(checkedButton.text){
                "Using GPS"->{
                    editor.putString("Location","Using GPS")
                    editor.apply()
                }
                "Using Map"->{
                    editor.putString("Location","Using Map")
                    editor.apply()
                }
            }
        }
    }
}