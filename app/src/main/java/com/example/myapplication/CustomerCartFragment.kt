package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityCustomerBinding
import com.example.myapplication.databinding.FragmentCustomerCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast.makeText as toastMakeText

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [CustomerCartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomerCartFragment : Fragment() {



    private lateinit var adapter: CartAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var CartList:ArrayList<Item>
    private lateinit var customerDb: DatabaseReference
    private lateinit var itemsDb: DatabaseReference
    private lateinit var AuthFireBase: FirebaseAuth
    private lateinit var order:Button
    private lateinit var delete:Button

    private var qua=15000






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_cart, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("hh","1")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        order=view.findViewById(R.id.placeOrder)
        delete=view.findViewById(R.id.deletecart)

        customerDb=FirebaseDatabase.getInstance().getReference("Customers")
        itemsDb=FirebaseDatabase.getInstance().getReference("items")

        AuthFireBase= FirebaseAuth.getInstance()
        val ID=(AuthFireBase.uid).toString()



        CartList= arrayListOf<Item>()
        customerDb.child(ID).child("cart").get().addOnSuccessListener {
            for (i in it.children){
                var itemName=i.child("name").getValue().toString()
                var itemPrice=i.child("price").getValue().toString()
                var itemQuantity=i.child("quantity").getValue().toString()
                var sellerId=i.child("sellerId").getValue().toString()

                CartList.add(Item(R.drawable.cheese,itemName,itemPrice,itemQuantity, sellerId ))


            }
            val layoutManager= LinearLayoutManager(context)
            recyclerView=view.findViewById(R.id.CustomerHomeRecyclerView)
            recyclerView.layoutManager=layoutManager
            adapter= CartAdapter(CartList)
            recyclerView.adapter=adapter
        }
        order.setOnClickListener {
            for(k in CartList.indices){
                var itemm = CartList[k].itemName
                var quan = CartList[k].itemQuantity.toInt()
                itemsDb.child(itemm).get().addOnSuccessListener {
                    qua = it.child("quantity").getValue().toString().toInt()
                    itemsDb.child(itemm).child("quantity").setValue((qua-quan).toString())
                }
            }
            customerDb.child(ID).child("cart").setValue(null)


            }
        delete.setOnClickListener {
            customerDb.child(ID).child("cart").setValue(null)
        }




    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CustomerHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomerHomeFragment().apply {
                arguments = Bundle().apply {
                    //putString(ARG_PARAM1, param1)
                    //putString(ARG_PARAM2, param2)
                }
            }

    }


}