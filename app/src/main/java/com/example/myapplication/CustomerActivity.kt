package com.example.myapplication

import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentController
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment


import com.example.myapplication.databinding.ActivityCustomerBinding
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class CustomerActivity : AppCompatActivity() {

    private lateinit var Binding:ActivityCustomerBinding
    private lateinit var AuthFireBase: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Binding = ActivityCustomerBinding.inflate(layoutInflater)

        AuthFireBase= FirebaseAuth.getInstance()


        setContentView(Binding.root)
        ReplaceFragment(CustomerHomeFragment())

        var Cnavbar=Binding.CustomerNavBar






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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.itemExitApp -> {
                finish()
                AuthFireBase.signOut()
                val intentBack= Intent(this,SignIn::class.java)
                startActivity(intentBack)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}


