package com.example.myapplication

import android.content.ClipData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentController
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment


import com.example.myapplication.databinding.ActivityCustomerBinding
import com.example.myapplication.databinding.ActivityMainBinding

class CustomerActivity : AppCompatActivity() {

    private lateinit var Binding:ActivityCustomerBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(Binding.root)
        ReplaceFragment(CustomerHomeFragment())
        Binding.CustomerNavBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.CustomerHome->ReplaceFragment(CustomerHomeFragment())
                R.id.CustomerCart->ReplaceFragment(CustomerCartFragment())
            }

            true
        }





    }

    private fun ReplaceFragment(fragment:Fragment){

        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.CustomerFrameLayout,fragment)
        fragmentTransaction.commit()

    }
}


