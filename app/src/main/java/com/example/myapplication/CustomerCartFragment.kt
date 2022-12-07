package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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

        var placeOrderTextView=view.findViewById<TextView>(R.id.TotalPrice)


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
                itemsDb.child(itemm).get().addOnSuccessListener {task->
                    Log.d("seeeeee","3")

                    qua = task.child("quantity").getValue().toString().toInt()
                    Log.d("seeeeee","23")
                    if(qua-quan==0){
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