package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private lateinit var Binding: ActivityProfileBinding
    private lateinit var AuthFireBase: FirebaseAuth
    private lateinit var dataBase: DatabaseReference
    private lateinit var CustomerDB: DatabaseReference
    private lateinit var SellerDB: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Binding= ActivityProfileBinding.inflate(layoutInflater)

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CustomerDB=FirebaseDatabase.getInstance().getReference("Customers")
        SellerDB=FirebaseDatabase.getInstance().getReference("Sellers")
        var found=false
        AuthFireBase= FirebaseAuth.getInstance()
        val ID=(AuthFireBase.uid).toString()
        CustomerDB.get().addOnSuccessListener {
            for(i in it.children){
                if((i.key).toString()==ID){
                    Log.d("hah","Customer")

                    found=true

                }
            }
        }
        Log.d("hah","hehe")
        if(found){
            Log.d("hah","hoho")
            CustomerDB.child(ID).child("found").setValue("yes")
        }

    }




}