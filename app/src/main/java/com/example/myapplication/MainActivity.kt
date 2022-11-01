package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.collections.Map

class MainActivity : AppCompatActivity() {
    private lateinit var Binding:ActivityMainBinding
    private lateinit var AuthFireBase:FirebaseAuth
    private lateinit var locationManager: LocationManager
    private lateinit var context:Context
    private var gpsStatus=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Binding= ActivityMainBinding.inflate(layoutInflater)
        AuthFireBase= FirebaseAuth.getInstance()
        setContentView(Binding.root)


        context=applicationContext
        locationManager= context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        Binding.TextView.text=gpsStatus.toString()




        Binding.SignOutButton.setOnClickListener {
            AuthFireBase.signOut()
            val intentSignOut= Intent(this,SignIn::class.java)
            startActivity(intentSignOut)
        }
        Binding.ProfileButton.setOnClickListener{
            val intentProfile=Intent(this,Profile::class.java)
            startActivity(intentProfile)
        }

        Binding.MapsButton.setOnClickListener{

            val intentMap=Intent(this,com.example.myapplication.Map::class.java)
            startActivity(intentMap)

           /* if(!gpsStatus){
                Thread(Runnable {val intentGPS= Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intentGPS)  }).start()

                recreate()
            }
            */
        }

        val ID=(AuthFireBase.uid).toString()
        Binding.textView2.text=ID
    }
}