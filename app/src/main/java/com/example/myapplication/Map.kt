package com.example.myapplication

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.graphics.red
import androidx.core.graphics.toColor
import com.example.myapplication.databinding.ActivityMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.view.MenuItem as MenuItem1

class Map : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var Binding:ActivityMapBinding
    private lateinit var AuthFireBase: FirebaseAuth
    private lateinit var sellerDataBase: DatabaseReference
    private lateinit var customerDataBase:DatabaseReference
    private lateinit var database:DatabaseReference
    private var found=false
    private var ID: String? =null
    private var markCount=0


    private lateinit var myMap: GoogleMap
    private lateinit var currentLocation:Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode=1

    private lateinit var locationManager: LocationManager
    private lateinit var context:Context
    private var gpsStatus=false

    ///check if the add marker button in the menu is selected or not
    private var addMarkerButton=false
    private var arrayListOfMarks=ArrayList<LatLng>()
    private var arrayListOfMarksinDB=ArrayList<LatLng>()


    data class myMark(val x:Double?=null ,val y:Double?=null)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Binding= ActivityMapBinding.inflate(layoutInflater) :I didnt use the Binding in this file
        setContentView(R.layout.activity_map)

        //Authentication stuff
        AuthFireBase= FirebaseAuth.getInstance()
        ID=(AuthFireBase.uid).toString()
        //Database stuff
        sellerDataBase=FirebaseDatabase.getInstance().getReference("Sellers")
        customerDataBase=FirebaseDatabase.getInstance().getReference("Customers")


        ///check if the User is found on the real time database
        ///he will be found if he has updated his profile and chose if he is a customer or seller
        customerDataBase.get().addOnSuccessListener {
            for(i in it.children){
                if((i.key).toString()==ID){
                    found=true
                    database=customerDataBase
                    database.child(ID.toString()).child("saved marks").get().addOnSuccessListener {
                        for (i in it.children){
                            markCount+=1
                            arrayListOfMarksinDB.add(LatLng(i.child("x").value.toString().toDouble(),i.child("y").value.toString().toDouble()))
                        }
                    }
                }
            }
        }
        if(!found){
            sellerDataBase.get().addOnSuccessListener {
                for(i in it.children){
                    if((i.key).toString()==ID){
                        found=true
                        database=sellerDataBase
                        database.child(ID.toString()).child("saved marks").get().addOnSuccessListener {
                            for (i in it.children){
                                markCount+=1
                                arrayListOfMarksinDB.add(LatLng(i.child("x").value.toString().toDouble(),i.child("y").value.toString().toDouble()))

                            }
                        }

                    }
                }
            }
        }


        ///to check if the user has enabled GPS or not
        ///if Gps is not enabled , he will be intented back to Main_Activity and asked to open GPS
        ///else, it will construct the map fragment
        context=applicationContext
        locationManager= context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
        if(!gpsStatus){
            Toast.makeText(this,"You need to enable GPS first",Toast.LENGTH_SHORT).show()
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{val mapFragment=supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)}
    }



    override fun onMapReady(p0: GoogleMap) {
        myMap=p0
        myMap.uiSettings.isZoomControlsEnabled=true
        myMap.uiSettings.isMyLocationButtonEnabled=true
        setUpMap()
        Log.d("hah","hhh1")






        /*
        if(found){
            Log.d("hah","hhh2")
            database.child(ID.toString()).child("saved marks").get().addOnSuccessListener {
                for (i in it.children){
                    var x=i.child("x").getValue().toString()
                    var y=i.child("y").getValue().toString()
                    var mark=MarkerOptions().position(LatLng(x.toDouble(),y.toDouble()))
                    myMap.addMarker(mark)


                }
            }
        }
        */


    }



    private fun setUpMap() {
        if((ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)&&(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), permissionCode)
            setUpMap()
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                myMap.isMyLocationEnabled = true
                currentLocation = it
                val coordinates = LatLng(currentLocation.latitude, currentLocation.longitude)
                val mark1 = MarkerOptions().position(coordinates)
                myMap.addMarker(mark1)
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates,16f))

            }
            else{
                setUpMap()
            }
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////
////to make use of the menu bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem1): Boolean {
        when(item.itemId){
            R.id.itemRefresh->{
                finish()
                startActivity(getIntent())
            }
            R.id.itemAddMarker->{
                if(!addMarkerButton){
                    Toast.makeText(this,"Click on map to add marker",Toast.LENGTH_LONG).show()
                    addMarkerButton=true
                    myMap.setOnMapClickListener {
                        addMarkerButton=false
                        myMap.addMarker(MarkerOptions().position(it))
                        arrayListOfMarks.add(it)
                        myMap.setOnMapClickListener (null)
                    }
                }
                else{
                    addMarkerButton=false
                    myMap.setOnMapClickListener (null)
                }

            }
            R.id.itemSave->{
                if (!found){
                    Toast.makeText(this,"Please update your profile to be able to save your marks",Toast.LENGTH_SHORT).show()
                }
                else{
                    var j=markCount+1
                    for(i in arrayListOfMarks){
                        var Mark=myMark(i.latitude,i.longitude)
                        database.child(ID.toString()).child("saved marks").child("mark"+j).setValue(Mark)
                        j += 1
                    }
                }

            }
            R.id.itemmyMarks->{
                if(found){
                    database.child(ID.toString()).child("saved marks").get().addOnSuccessListener {
                        for(i in it.children){
                            var x=i.child("x").getValue().toString().toDouble()
                            var y=i.child("y").getValue().toString().toDouble()
                            var mark=MarkerOptions().position(LatLng(x,y))
                            myMap.addMarker(mark)


                        }
                    }
                }


            }
        }
        return super.onOptionsItemSelected(item)
    }

}