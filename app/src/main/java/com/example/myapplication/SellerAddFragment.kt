package com.example.myapplication

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.myapplication.databinding.FragmentSellerAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*


class SellerAddFragment : Fragment() {
    private lateinit var binding: FragmentSellerAddBinding
    lateinit var ImageUri : Uri
    private lateinit var SellerDB: DatabaseReference
    private lateinit var itemsDb:DatabaseReference
    private lateinit var AuthFireBase: FirebaseAuth






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        SellerDB=FirebaseDatabase.getInstance().getReference("Sellers")
        itemsDb=FirebaseDatabase.getInstance().getReference("items")
        binding = FragmentSellerAddBinding.inflate(inflater, container, false)
        AuthFireBase= FirebaseAuth.getInstance()

        ///////To be used later


        binding.SelectImage.setOnClickListener {
            selectImage()

        }
        binding.NewItemAddBTN.setOnClickListener {
            if(binding.NewItemName.text.isEmpty() || binding.NewItemPrice.text.isEmpty() || binding.NewItemQuantity.text.isEmpty()||binding.imageView2.drawable==null ){
                Toast.makeText(requireActivity(),"Fill all fields",Toast.LENGTH_SHORT).show()
            }
            else{
                uploadImage()


            }
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun selectImage() {
        val intent=Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }
    private fun uploadImage(){
        val progressDialog=ProgressDialog(requireActivity())
        progressDialog.setMessage("Uploading File ....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter=SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now=Date()
        val fileName=formatter.format(now)
        val storageReference= FirebaseStorage.getInstance().getReference("images/$fileName")
        storageReference.putFile(ImageUri).addOnSuccessListener {
            binding.imageView2.setImageURI(null)
            Toast.makeText(requireActivity(),"Successfully uploaded",Toast.LENGTH_SHORT).show()
            if(progressDialog.isShowing) progressDialog.dismiss()
            SellerDB.child(AuthFireBase.uid.toString()).child("items").child(fileName.toString()).child("name").setValue(binding.NewItemName.text.toString())
            SellerDB.child(AuthFireBase.uid.toString()).child("items").child(fileName.toString()).child("price").setValue(binding.NewItemPrice.text.toString())
            SellerDB.child(AuthFireBase.uid.toString()).child("items").child(fileName.toString()).child("quantity").setValue(binding.NewItemQuantity.text.toString())

            itemsDb.child(fileName.toString()).child("name").setValue(binding.NewItemName.text.toString())
            itemsDb.child(fileName.toString()).child("price").setValue(binding.NewItemPrice.text.toString())
            itemsDb.child(fileName.toString()).child("quantity").setValue(binding.NewItemQuantity.text.toString())
            itemsDb.child(fileName.toString()).child("sellerId").setValue(AuthFireBase.uid.toString())






        }.addOnFailureListener{
            if(progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(requireActivity(),"Failed",Toast.LENGTH_SHORT).show()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==100 && resultCode== RESULT_OK){
            ImageUri=data?.data!!
            binding.imageView2.setImageURI(ImageUri)


        }
    }


    companion object {
        fun newInstance(param1: String, param2: String) =
            SellerAddFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}