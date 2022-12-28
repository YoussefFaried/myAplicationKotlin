package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityCustomerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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
    private lateinit var sellerDb: DatabaseReference
    private lateinit var itemsDb: DatabaseReference
    private lateinit var AuthFireBase: FirebaseAuth
    private lateinit var order:Button
    private lateinit var delete:Button
    private lateinit var Binding:ActivityCustomerBinding
    private var sellerHashSet= mutableSetOf<String>()




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

        var placeOrderTextView=view.findViewById<TextView>(R.id.itemspricenum)
        var itemsPrice=view.findViewById<TextView>(R.id.itemspricenum)
        var deliveryPrice=view.findViewById<TextView>(R.id.deliverycostnum)
        var totalPrice=view.findViewById<TextView>(R.id.totalpricenum)
        var delivery=0.0


        customerDb=FirebaseDatabase.getInstance().getReference("Customers")
        itemsDb=FirebaseDatabase.getInstance().getReference("items")
        sellerDb=FirebaseDatabase.getInstance().getReference("Sellers")




        AuthFireBase= FirebaseAuth.getInstance()
        val ID=(AuthFireBase.uid).toString()


        Log.d("heheheheh","3222")
        //placeOrderTextView.setText(customerDb.child(ID).child("cart").child("totalprice"))

        customerDb.child(ID).child("cart").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                placeOrderTextView.setText(snapshot.child("totalPrice").getValue().toString())
                if(placeOrderTextView.text.toString().equals("null")) {           placeOrderTextView.setText("0")}

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        customerDb.child(ID).get().addOnSuccessListener {
            var myX=it.child("location").child("x").getValue().toString().toDouble()
            var myY=it.child("location").child("y").getValue().toString().toDouble()
            customerDb.child(ID).child("cart").child("items").get().addOnSuccessListener {

                for(i in it.children){
                    sellerHashSet.add(i.child("sellerId").getValue().toString())
                    Log.d("hehe","555")

                }
                sellerDb.get().addOnSuccessListener {
                    for (j in it.children){
                        if(sellerHashSet.contains(j.key.toString()) && j.hasChild("location")){
                            var hisX=j.child("location").child("x").getValue().toString().toDouble()
                            var hisY=j.child("location").child("y").getValue().toString().toDouble()
                            var distance=distanceInKm(myX,myY,hisX,hisY)
                            var deliverySpecific=(distance*10).toInt()
                            delivery+=deliverySpecific
                        }
                    }
                    deliveryPrice.text=delivery.toString()
                }

            }


        }







        CartList= arrayListOf<Item>()
        customerDb.child(ID).child("cart").child("items").get().addOnSuccessListener {
            for (i in it.children){
                var itemName=i.child("name").getValue().toString()
                var itemPrice=i.child("price").getValue().toString()
                var itemQuantity=i.child("quantity").getValue().toString()
                var sellerId=i.child("sellerId").getValue().toString()
                var itemId=i.key.toString()

                CartList.add(Item(R.drawable.cheese,itemName,itemPrice,itemQuantity, sellerId,itemId  ))


            }
            val layoutManager= LinearLayoutManager(context)
            recyclerView=view.findViewById(R.id.CustomerHomeRecyclerView)
            recyclerView.layoutManager=layoutManager
            adapter= CartAdapter(CartList)
            recyclerView.adapter=adapter
        }


        order.setOnClickListener {
            Log.d("seeeeee","1")
            for(k in CartList.indices){
                Log.d("seeeeee","2")
                var itemm = CartList[k].itemId
                var quan = CartList[k].itemQuantity.toInt()
                var sellerid = CartList[k].sellerId
                var itemName=CartList[k].itemName
                itemsDb.child(itemm).get().addOnSuccessListener {task->
                    Log.d("seeeeee","3")
                    qua=-10
                    if(task.exists()){
                        qua = task.child("quantity").getValue().toString().toInt()
                    }

                    Log.d("seeeeee","23")

                    if(qua==-10||(qua<quan)){
                        Toast.makeText(requireActivity(),itemName+" is out of stock",Toast.LENGTH_SHORT).show()
                    }
                    else if(qua-quan==0){
                        Log.d("seeeeee","4")

                        itemsDb.child(itemm).setValue(null)
                        sellerDb.child(sellerid).child("items").child(itemm).setValue(null)

                    }
                    else{
                        Log.d("seeeeee","5")

                        itemsDb.child(itemm).child("quantity").setValue((qua-quan).toString())
                        sellerDb.child(sellerid).child("items").child(itemm).child("quantity").setValue((qua-quan).toString())
                        Log.d("seeeeee","6")

                    }
                }
            }

            customerDb.child(ID).child("cart").setValue(null)
            itemsDb.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.CustomerFrameLayout, CustomerHomeFragment())
                    transaction?.disallowAddToBackStack()
                    transaction?.commit()

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })



            /*var CustomerActivity=CustomerActivity()
            CustomerActivity.Cnavbar.selectedItemId=R.id.CustomerHome*/
        }


        delete.setOnClickListener {
            customerDb.child(ID).child("cart").setValue(null)
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.CustomerFrameLayout, CustomerHomeFragment())
            transaction?.disallowAddToBackStack()
            transaction?.commit()
        }

        deliveryPrice.addTextChangedListener {
            if(deliveryPrice.text.isNotEmpty()&& itemsPrice.text.isNotEmpty()){
                totalPrice.text=(deliveryPrice.text.toString().toDouble()+itemsPrice.text.toString().toDouble()).toString()
            }
        }




    }
    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
    fun distanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        dist = dist * 1.609344
        return dist
    }
    fun distance(lat_a: Double, lng_a: Double, lat_b: Double, lng_b: Double): Double {
        val earthRadius = 3958.75
        val latDiff = Math.toRadians((lat_b - lat_a).toDouble())
        val lngDiff = Math.toRadians((lng_b - lng_a).toDouble())
        val a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(lat_a.toDouble())) * Math.cos(Math.toRadians(lat_b.toDouble())) *
                Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val distance = earthRadius * c
        val meterConversion = 1609
        return (distance * meterConversion.toFloat()).toDouble()
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