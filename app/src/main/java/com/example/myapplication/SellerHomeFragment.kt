package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SellerHomeFragment : Fragment() {
    private lateinit var adapter: AdapterSeller
    private lateinit var recyclerView: RecyclerView
    private lateinit var sellList:ArrayList<Item>
    private lateinit var AuthFireBase: FirebaseAuth
    private lateinit var dataBase: DatabaseReference
    private lateinit var SellerDB: DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AuthFireBase= FirebaseAuth.getInstance()


        ///////To be used later
        val ID=(AuthFireBase.uid).toString()
        sellList= arrayListOf<Item>()
        SellerDB=FirebaseDatabase.getInstance().getReference("Sellers")

        SellerDB.child(ID).child("items").get().addOnSuccessListener {
            for(i in it.children){
                var name=i.child("name").getValue().toString()
                var price=i.child("price").getValue().toString()
                var quantity=i.child("quantity").getValue().toString()
                var sellerId=ID
                var itemId=i.key.toString()

                sellList.add(Item(R.drawable.cheese,name,price,quantity,sellerId,itemId))




            }
            val layoutManager= LinearLayoutManager(context)
            recyclerView= view.findViewById(R.id.SellerHomeRecyclerView)
            recyclerView.layoutManager=layoutManager
            adapter= AdapterSeller(sellList)
            recyclerView.adapter=adapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_home, container, false)
    }






    companion object {

        fun newInstance(param1: String, param2: String) =
            SellerHomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}