package com.example.myapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MyAdapter (private val itemList : ArrayList<Item>): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){
    private lateinit var customerDb:DatabaseReference
    private lateinit var AuthFireBase: FirebaseAuth


    override fun  onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.iteeem,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        customerDb=FirebaseDatabase.getInstance().getReference("Customers")
        AuthFireBase= FirebaseAuth.getInstance()
        val ID=(AuthFireBase.uid).toString()

       val currentItem = itemList[position]
        holder.itemImg.setImageResource(currentItem.itemImg)
        holder.itemName.text=currentItem.itemName
        holder.itemPrice.text=currentItem.itemPrice
        holder.itemQuantity.text=""
        holder.removebtn.isEnabled=false
        holder.addbtn.isEnabled=true


        holder.addbtn.setOnClickListener {
            var price=currentItem.itemPrice.toDouble()
            var quantity=currentItem.itemQuantity.toInt()
            Log.d("ggg","a7a")
            if (holder.itemQuantity.text.toString()==""){
                Log.d("ggg","aaa")
                holder.itemQuantity.text="1"
            }
            else{
                Log.d("ggg","a33a")
                holder.itemQuantity.text=(holder.itemQuantity.text.toString().toInt()+1).toString()
                if(holder.itemQuantity.text.toString().toInt()==quantity){
                    holder.addbtn.isEnabled=false
                }
                else{
                    Log.d("ggg","a9876")
                    holder.addbtn.isEnabled=true
                }

            }
            holder.removebtn.isEnabled=true
            customerDb.child(ID.toString()).child("cart").child(holder.itemName.text.toString()).child("name").setValue(holder.itemName.text.toString())
            customerDb.child(ID.toString()).child("cart").child(holder.itemName.text.toString()).child("price").setValue(holder.itemPrice.text.toString())
            customerDb.child(ID.toString()).child("cart").child(holder.itemName.text.toString()).child("quantity").setValue(holder.itemQuantity.text.toString())

        }
        holder.removebtn.setOnClickListener {
            Log.d("ggg","nn")
            var price=currentItem.itemPrice.toDouble()
            var quantity=currentItem.itemQuantity.toInt()
            if(holder.itemQuantity.text.toString().toInt()==1){
                Log.d("ggg","pp6")
                holder.itemQuantity.text=""
                holder.removebtn.isEnabled=false
            }
            else {
                Log.d("ggg","mmm")
                holder.itemQuantity.text =(holder.itemQuantity.text.toString().toInt() - 1).toString()
                holder.removebtn.isEnabled = true
            }
            //if(holder.itemQuantity.text.toString().toInt()<quantity){
                holder.addbtn.isEnabled=true
            customerDb.child(ID.toString()).child("cart").child(holder.itemName.text.toString()).child("name").setValue(holder.itemName.text.toString())
            customerDb.child(ID.toString()).child("cart").child(holder.itemName.text.toString()).child("price").setValue(holder.itemPrice.text.toString())
            customerDb.child(ID.toString()).child("cart").child(holder.itemName.text.toString()).child("quantity").setValue(holder.itemQuantity.text.toString())


            //}
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        val itemImg : ImageView =itemView.findViewById(R.id.itemImageView)
        val itemName : TextView =itemView.findViewById(R.id.itemName)
        val itemPrice : TextView =itemView.findViewById(R.id.itemPrice)
        val itemQuantity : TextView =itemView.findViewById(R.id.itemQuantity)
        val removebtn : TextView =itemView.findViewById(R.id.remove)
        val addbtn : TextView =itemView.findViewById(R.id.add)


    }
}