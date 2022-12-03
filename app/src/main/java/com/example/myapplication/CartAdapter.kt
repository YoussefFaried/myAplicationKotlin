package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter (private val CartList:ArrayList<Item>):RecyclerView.Adapter<CartAdapter.cartViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.iteeeemcart,parent,false)
        return cartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: cartViewHolder, position: Int) {
        val currentItem=CartList[position]
        holder.itemImg.setImageResource(currentItem.itemImg)
        holder.itemName.text=currentItem.itemName
        holder.itemPrice.text=currentItem.itemPrice
        holder.itemQuantity.text=currentItem.itemQuantity


    }

    override fun getItemCount(): Int {
        return CartList.size
    }




    class cartViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val itemImg : ImageView =itemView.findViewById(R.id.cartItemImageView)
        val itemName : TextView =itemView.findViewById(R.id.cartItemName)
        val itemPrice : TextView =itemView.findViewById(R.id.cartItemPrice)
        val itemQuantity : TextView =itemView.findViewById(R.id.cartItemQuantity)
        val deleteBtn:Button=itemView.findViewById(R.id.cartDeleteItemBtn)


    }
}