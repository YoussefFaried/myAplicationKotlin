package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class CartAdapter (private val CartList:ArrayList<Item>):RecyclerView.Adapter<CartAdapter.cartViewHolder>(){
    var totalPrice=0
    private lateinit var customerDb:DatabaseReference
    private lateinit var AuthFireBase:FirebaseAuth
    private lateinit var StorageRef: StorageReference




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.iteeeemcart,parent,false)


        return cartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: cartViewHolder, position: Int) {
        customerDb=FirebaseDatabase.getInstance().getReference("Customers")
        AuthFireBase= FirebaseAuth.getInstance()
        val ID=(AuthFireBase.uid).toString()
        val currentItem=CartList[position]
        holder.itemImg.setImageResource(currentItem.itemImg)
        holder.itemName.text=currentItem.itemName
        holder.itemPrice.text=currentItem.itemPrice
        holder.itemQuantity.text=currentItem.itemQuantity
        var sellerId=currentItem.sellerId
        totalPrice+=currentItem.itemPrice.toInt()*currentItem.itemQuantity.toInt()
        customerDb.child(ID).child("cart").child("totalPrice").setValue(totalPrice)
        StorageRef = FirebaseStorage.getInstance().reference
        StorageRef.child("images/${currentItem.itemId}").downloadUrl.addOnSuccessListener {

            Picasso.get().load(it).into(holder.itemImg)
        }

    }

    override fun getItemCount(): Int {

        return CartList.size
    }




    class cartViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val itemImg : ImageView =itemView.findViewById(R.id.cartItemImageView)
        val itemName : TextView =itemView.findViewById(R.id.cartItemName)
        val itemPrice : TextView =itemView.findViewById(R.id.cartItemPrice)
        val itemQuantity : TextView =itemView.findViewById(R.id.cartItemQuantity)



    }
}