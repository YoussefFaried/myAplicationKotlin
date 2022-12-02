package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivityCustomerBinding
import com.example.myapplication.databinding.ActivitySellerBinding
import com.google.firebase.auth.FirebaseAuth

class SellerActivity : AppCompatActivity() {
    private lateinit var Binding: ActivitySellerBinding
    private lateinit var AuthFireBase:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AuthFireBase= FirebaseAuth.getInstance()


        Binding = ActivitySellerBinding.inflate(layoutInflater)
        setContentView(Binding.root)
        ReplaceFragment(SellerHomeFragment())
        Binding.SellerNavBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.SellerHome->ReplaceFragment(SellerHomeFragment())
                R.id.SellerAddItem->ReplaceFragment((SellerAddFragment()))
            }
            true
        }

    }
    private fun ReplaceFragment(fragment: Fragment){

        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.SellerFrameLayout,fragment)
        fragmentTransaction.commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.itemExitApp -> {
                AuthFireBase.signOut()
                finish()
                val intentBack= Intent(this,SignIn::class.java)
                startActivity(intentBack)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}