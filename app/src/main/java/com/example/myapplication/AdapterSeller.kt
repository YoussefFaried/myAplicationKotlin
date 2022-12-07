package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class AdapterSeller(private val sellerList:ArrayList<Item>): RecyclerView.Adapter<AdapterSeller.MyViewHolder>() {
    private lateinit var sellerDb: DatabaseReference
    private lateinit var itemsDb: DatabaseReference
    private lateinit var AuthFireBase: FirebaseAuth
    private lateinit var StorageRef: StorageReference




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val itemview=LayoutInflater.from(parent.context).inflate(R.layout.iteeeemseller,
           parent,false)
        sellerDb= FirebaseDatabase.getInstance().getReference("Sellers")
        itemsDb= FirebaseDatabase.getInstance().getReference("items")
        AuthFireBase= FirebaseAuth.getInstance()
        val ID=(AuthFireBase.uid).toString()
        return MyViewHolder(itemview)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=sellerList[position]
        var sellerId=currentItem.sellerId
        holder.itemImg.setImageResource(currentItem.itemImg)
        holder.itemName.text=currentItem.itemName
        holder.itemPrice.text=currentItem.itemPrice
        holder.itemQuantity.text=currentItem.itemQuantity




        holder.itemName.isEnabled=false
        holder.itemPrice.isEnabled=false
        holder.itemQuantity.isEnabled=false
        holder.itemImg.isEnabled=false
        holder.updatebtn.isEnabled=true
        holder.itemImg.isClickable=false

        StorageRef = FirebaseStorage.getInstance().reference
        StorageRef.child("images/${currentItem.itemId}").downloadUrl.addOnSuccessListener {

            Picasso.get().load(it).into(holder.itemImg)
        }


        holder.updatebtn.setOnClickListener {
            holder.updatebtn.isEnabled=false
            holder.savebtn.isEnabled=true
            holder.itemName.isEnabled=true
            holder.itemPrice.isEnabled=true
            holder.itemQuantity.isEnabled=true
            holder.itemImg.isClickable=true

        }
        holder.itemImg.setOnClickListener {
            val intent= Intent()
            intent.type="image/*"
            intent.action= Intent.ACTION_GET_CONTENT
            Activity().startActivityForResult(intent,100)

        }
        holder.savebtn.setOnClickListener {

            if(holder.itemName.text.isNotEmpty() && holder.itemPrice.text.isNotEmpty() && holder.itemQuantity.text.isNotEmpty()){

                holder.itemName.isEnabled=false
                holder.itemPrice.isEnabled=false
                holder.itemQuantity.isEnabled=false
                holder.itemImg.isEnabled=false
                holder.updatebtn.isEnabled=true
                holder.itemImg.isClickable=false

                sellerDb.child(AuthFireBase.uid.toString()).child("items").child(currentItem.itemId).child("name").setValue(holder.itemName.text.toString())
                sellerDb.child(AuthFireBase.uid.toString()).child("items").child(currentItem.itemId).child("price").setValue(holder.itemPrice.text.toString())
                sellerDb.child(AuthFireBase.uid.toString()).child("items").child(currentItem.itemId).child("quantity").setValue(holder.itemQuantity.text.toString())

                itemsDb.child(currentItem.itemId).child("name").setValue(holder.itemName.text.toString())
                itemsDb.child(currentItem.itemId).child("price").setValue(holder.itemPrice.text.toString())
                itemsDb.child(currentItem.itemId).child("quantity").setValue(holder.itemQuantity.text.toString())
                itemsDb.child(currentItem.itemId).child("sellerId").setValue(AuthFireBase.uid.toString())
            }

        }

    }

    override fun getItemCount(): Int {
        return sellerList.size
    }

    class MyViewHolder(itemview: View): RecyclerView.ViewHolder(itemview)
    {
        val itemImg : ImageView =itemView.findViewById(R.id.updateItemImageView)
        val itemName : TextView =itemView.findViewById(R.id.updateItemName)
        val itemPrice : TextView =itemView.findViewById(R.id.updateItemPrice)
        val itemQuantity : TextView =itemView.findViewById(R.id.updateItemQuantity)
        val updatebtn : TextView =itemView.findViewById(R.id.sellerUpdate)
        val savebtn : TextView =itemView.findViewById(R.id.sellerSave)

    }



}