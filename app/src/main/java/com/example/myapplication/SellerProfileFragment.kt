package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.databinding.ActivityProfileBinding
import com.example.myapplication.databinding.FragmentProfileBinding
import com.example.myapplication.databinding.FragmentSellerProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SellerProfileFragment : Fragment() {
    private lateinit var Binding: FragmentSellerProfileBinding
    private lateinit var AuthFireBase: FirebaseAuth
    private lateinit var dataBase: DatabaseReference
    private lateinit var CustomerDB: DatabaseReference
    private lateinit var SellerDB: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Binding= FragmentSellerProfileBinding.inflate(layoutInflater)

        AuthFireBase= FirebaseAuth.getInstance()


        ///////To be used later
        val ID=(AuthFireBase.uid).toString()

        data class User(val firstname:String?=null ,val lastname:String?=null,val age:String?=null,val phone:String?=null)
        //////



        //////retrieving data from database
        //////the user personal info will be found if he has updated his profile once at least after signing up

        CustomerDB= FirebaseDatabase.getInstance().getReference("Customers")
        SellerDB= FirebaseDatabase.getInstance().getReference("Sellers")
        var found=false
        CustomerDB.get().addOnSuccessListener {
            for(i in it.children){
                if((i.key).toString()==ID){
                    Binding.Customer.isChecked=true
                    Binding.Customer.isEnabled=false
                    Binding.Seller.isEnabled=false
                    Binding.FirstName.setText(i.child("personal info").child("firstname").getValue().toString())
                    Binding.LastName.setText(i.child("personal info").child("lastname").getValue().toString())
                    Binding.Age.setText(i.child("personal info").child("age").getValue().toString())
                    Binding.Phone2.setText(i.child("personal info").child("phone").getValue().toString())
                    found=true

                }
            }
        }
        if(!found){
            SellerDB.get().addOnSuccessListener {
                for(i in it.children){
                    if((i.key).toString()==ID){
                        Binding.Seller.isChecked=true
                        Binding.Customer.isEnabled=false
                        Binding.Seller.isEnabled=false
                        Binding.FirstName.setText(i.child("personal info").child("firstname").getValue().toString())
                        Binding.LastName.setText(i.child("personal info").child("lastname").getValue().toString())
                        Binding.Age.setText(i.child("personal info").child("age").getValue().toString())
                        Binding.Phone2.setText(i.child("personal info").child("phone").getValue().toString())

                    }
                }
            }
        }









        ///////eventlisteners





        // Inflate the layout for this fragment

        return Binding.root
    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            SellerProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}