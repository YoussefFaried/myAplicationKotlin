package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER




/**
 * A simple [Fragment] subclass.
 * Use the [CustomerHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomerHomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    //private var param1: String? = null
    //private var param2: String? = null

    private lateinit var customerDb:DatabaseReference
    private lateinit var sellerDb:DatabaseReference
    private lateinit var itemsDb:DatabaseReference
    private lateinit var AuthFireBase: FirebaseAuth


    private lateinit var adapter: MyAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemList:ArrayList<Item>
    lateinit var imgId :Array<Int>
    lateinit var nameofitem :Array<String>
    lateinit var price :Array<String>
    lateinit var quantity :Array<String>










    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
            //param2 = it.getString(ARG_PARAM2)
        }
    }








    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_home, container, false)
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








    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        customerDb=FirebaseDatabase.getInstance().getReference("Customers")
        sellerDb=FirebaseDatabase.getInstance().getReference("Sellers")
        itemsDb=FirebaseDatabase.getInstance().getReference("items")
        AuthFireBase= FirebaseAuth.getInstance()
        val ID=(AuthFireBase.uid).toString()




        val targetSellers= arrayListOf<String>()
        val storage=FirebaseStorage.getInstance().reference.child("images/roomy.jpg")
        Log.d("ggg","yes")
        var flag1=false
        var flag2=false

        itemList= arrayListOf<Item>()
        customerDb.child(ID).child("cart").child("totalPrice").setValue("0")

        itemsDb.get().addOnSuccessListener {
            for(i in it.children){
                var name=i.child("name").getValue().toString()
                var price=i.child("price").getValue().toString()
                var quantity=i.child("quantity").getValue().toString()
                var sellerId=i.child("sellerId").getValue().toString()
                var itemId=i.key.toString()


                itemList.add(Item(R.drawable.cheese,name,price,quantity,sellerId,itemId))
            }
            val layoutManager=LinearLayoutManager(context)
            recyclerView=view.findViewById(R.id.CustomerHomeRecyclerView)
            recyclerView.layoutManager=layoutManager
            adapter= MyAdapter(itemList)
            recyclerView.adapter=adapter

        }


        /*sellerDb.get().addOnSuccessListener{
            Log.d("ggg", "yes2")
            for (i in it.children) {
                Log.d("ggg", "yes3")
                if (i.hasChild("items")) {
                    Log.d("ggg", "yes4")
                    sellerDb.child(i.value.toString()).child("items").get().addOnSuccessListener {task->
                        Log.d("ggg","yes6")

                        for(j in task.children){
                            Log.d("ggg","yes5")

                            itemList.add(Item(R.drawable.cheese,j.child("name").getValue().toString(),j.child("price").toString(),j.child("quantity").toString()))
                        }
                    }

                    var kkkk=sellerDb.child(i.toString()).child("items").child("roomy")
                    itemList.add(Item(R.drawable.cheese,kkkk.child("name").toString(),kkkk.child("price").toString(),kkkk.child("quantity").toString()))


                    targetSellers.add(i.getValue().toString())
                }
            }
            val layoutManager=LinearLayoutManager(context)
            recyclerView=view.findViewById(R.id.CustomerHomeRecyclerView)
            recyclerView.layoutManager=layoutManager
            adapter= MyAdapter(itemList)
            recyclerView.adapter=adapter
            flag1=true

        }



        for(k in targetSellers.indices){
            Log.d("ggg","yaaaaaaeees1")
            sellerDb.child(targetSellers[k]).child("items").get().addOnSuccessListener {
                for (i in it.children){
                    if(i.key=="roomy"){
                        Log.d("ggg","yaaaaaaeees2")
                    }
                }

            }
        }

*/





        //dataInitilize()
        /*val layoutManager=LinearLayoutManager(context)
        recyclerView=view.findViewById(R.id.CustomerHomeRecyclerView)
        recyclerView.layoutManager=layoutManager
        adapter= MyAdapter(itemList)
        recyclerView.adapter=adapter*/
    }








/*
    private fun dataInitilize(){
        itemList= arrayListOf<Item>()


        imgId=arrayOf(
            R.drawable.cheese,
            R.drawable.cheese,
            R.drawable.cheese

        )

        nameofitem= arrayOf(
            "Roomy",
            "batates",
            "lanshon"

        )
        price= arrayOf(
            "140",
            "20",
            "25"

        )
        quantity= arrayOf(
            "",
            "",
            ""

        )
        for(i in imgId.indices){
            val item=Item(imgId[i],nameofitem[i],price[i],quantity[i])
            itemList.add(item)
        }

    }
    */

}