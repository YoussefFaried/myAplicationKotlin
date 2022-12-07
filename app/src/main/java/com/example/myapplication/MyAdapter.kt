package com.example.myapplication

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewConfiguration.get
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.ActionBarPolicy.get
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.AppCompatDrawableManager.get
import androidx.appcompat.widget.ResourceManagerInternal.get
import androidx.lifecycle.ViewTreeLifecycleOwner.get
import androidx.lifecycle.ViewTreeViewModelStoreOwner.get
import androidx.recyclerview.widget.RecyclerView
import androidx.savedstate.ViewTreeSavedStateRegistryOwner.get
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.lang.reflect.Array.get
import com.squareup.picasso.Picasso as Picasso

class MyAdapter (private val itemList : ArrayList<Item>): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){
    private lateinit var customerDb:DatabaseReference
    private lateinit var AuthFireBase: FirebaseAuth
    private lateinit var StorageRef: StorageReference



    override fun  onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.iteeem,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        customerDb=FirebaseDatabase.getInstance().getReference("Customers")
        AuthFireBase= FirebaseAuth.getInstance()
        val ID=(AuthFireBase.uid).toString()
       val currentItem = itemList[position]



        var sellerId=currentItem.sellerId
        holder.itemName.text=currentItem.itemName
        holder.itemPrice.text=currentItem.itemPrice
        holder.itemQuantity.text=""
        holder.removebtn.isEnabled=false
        holder.addbtn.isEnabled=true

        /*var storageRef=FirebaseStorage.getInstance().reference.child("items/$currentItem.itemId")

        var localFile= File.createTempFile("tempImage","jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            var bitmap=BitmapFactory.decodeFile(localFile.absolutePath)
            holder.itemImg.setImageBitmap(bitmap)
            holder.itemImg.setImageResource(R.drawable.cheese)

        }*/
        /*var localfile = File.createTempFile("image", "jpeg")
        StorageRef = FirebaseStorage.getInstance().reference
        StorageRef.child("image/${currentItem.itemId}.jpg").getFile(localfile).addOnSuccessListener {
            holder.itemImg.setImageBitmap(BitmapFactory.decodeFile(localfile.absolutePath))
        }*/
        StorageRef = FirebaseStorage.getInstance().reference
        StorageRef.child("images/${currentItem.itemId}").downloadUrl.addOnSuccessListener {

            Picasso.get().load(it).into(holder.itemImg)
        }

        holder.addbtn.setOnClickListener {
            var price=currentItem.itemPrice.toDouble()
            var quantity=currentItem.itemQuantity.toInt()
            Log.d("ggg","aaaa")
            if (holder.itemQuantity.text.toString()==""){
                Log.d("ggg","aaa")
                holder.itemQuantity.text="1"
            }

            else{
                Log.d("ggg","a33a")
                holder.itemQuantity.text=(holder.itemQuantity.text.toString().toInt()+1).toString()
            }
            if(holder.itemQuantity.text.toString().toInt()==quantity){
                holder.addbtn.isEnabled=false
            }
            holder.removebtn.isEnabled=true
            customerDb.child(ID.toString()).child("cart").child("items").child(currentItem.itemId.toString()).child("name").setValue(holder.itemName.text.toString())
            customerDb.child(ID.toString()).child("cart").child("items").child(currentItem.itemId.toString()).child("price").setValue(holder.itemPrice.text.toString())
            customerDb.child(ID.toString()).child("cart").child("items").child(currentItem.itemId.toString()).child("quantity").setValue(holder.itemQuantity.text.toString())
            customerDb.child(ID.toString()).child("cart").child("items").child(currentItem.itemId.toString()).child("sellerId").setValue(currentItem.sellerId)

        }
        holder.removebtn.setOnClickListener {
            Log.d("ggg","nn")
            var price=currentItem.itemPrice.toDouble()
            var quantity=currentItem.itemQuantity.toInt()
            if(holder.itemQuantity.text.toString().toInt()==1){
                Log.d("ggg","pp6")
                holder.itemQuantity.text=""
                holder.removebtn.isEnabled=false
                customerDb.child(ID.toString()).child("cart").child("items").child(currentItem.itemId).setValue(null)

            }
            else {
                Log.d("ggg","mmm")
                holder.itemQuantity.text =(holder.itemQuantity.text.toString().toInt() - 1).toString()
                holder.removebtn.isEnabled = true
                customerDb.child(ID.toString()).child("cart").child("items").child(currentItem.itemId.toString()).child("name").setValue(holder.itemName.text.toString())
                customerDb.child(ID.toString()).child("cart").child("items").child(currentItem.itemId.toString()).child("price").setValue(holder.itemPrice.text.toString())
                customerDb.child(ID.toString()).child("cart").child("items").child(currentItem.itemId.toString()).child("quantity").setValue(holder.itemQuantity.text.toString())


            }
            //if(holder.itemQuantity.text.toString().toInt()<quantity){
                holder.addbtn.isEnabled=true

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