package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import com.example.myapplication.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignIn : AppCompatActivity() {


    private lateinit var Binding:ActivitySignInBinding
    private lateinit var AuthFireBase:FirebaseAuth
    private lateinit var CustomerDB:DatabaseReference
    private lateinit var SellerDB:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Binding= ActivitySignInBinding.inflate(layoutInflater)
        setContentView(Binding.root)

        AuthFireBase= FirebaseAuth.getInstance()

        CustomerDB=FirebaseDatabase.getInstance().getReference("Customers")
        SellerDB=FirebaseDatabase.getInstance().getReference("Sellers")

        Binding.SignUpLabel.setOnClickListener {
            Binding.progressBar.visibility= VISIBLE
            val intent1 = Intent(this, SignUp::class.java)
            finish()
            startActivity(intent1)

        }
        Binding.SignInButton.setOnClickListener{
            Binding.progressBar.visibility= VISIBLE

            val Email=Binding.SignInEmail.text.toString()
            val Password=Binding.SignInPassword.text.toString()
            if(Email.isNotEmpty() && Password.isNotEmpty() ){
                AuthFireBase.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(){
                    if(it.isSuccessful){
                        val id=AuthFireBase.uid.toString()

                        SellerDB.get().addOnSuccessListener {
                            for (i in it.children){
                                if(i.key.toString()==id){
                                    val intentSeller=Intent(this,SellerActivity::class.java)
                                    finish()
                                    startActivity(intentSeller)
                                }
                            }
                        }

                        CustomerDB.get().addOnSuccessListener {
                            for (i in it.children){
                                if(i.key.toString()==id){
                                    val intentCustomer=Intent(this,CustomerActivity::class.java)
                                    finish()
                                    startActivity(intentCustomer)
                                }
                            }
                        }



                    }
                    else{
                        Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        Binding.progressBar.visibility= INVISIBLE
                    }
                }

            }
            else{
                Toast.makeText(this,"Some Fields are empty",Toast.LENGTH_SHORT).show()
                Binding.progressBar.visibility= INVISIBLE
            }

        }
    }

    override fun onStart() {
        super.onStart()
        if(AuthFireBase.currentUser!=null){
            val id=AuthFireBase.uid.toString()

            SellerDB.get().addOnSuccessListener {
                for (i in it.children){
                    if(i.key.toString()==id){
                        val intentSeller=Intent(this,SellerActivity::class.java)
                        val intentMap=Intent(this,Map::class.java)
                        finish()
                        if(i.hasChild("location")){
                            startActivity(intentSeller)
                        }
                        else{
                            startActivity(intentMap)
                        }

                    }
                }
            }

            CustomerDB.get().addOnSuccessListener {
                for (i in it.children){
                    if(i.key.toString()==id){
                        val intentCustomer=Intent(this,CustomerActivity::class.java)
                        val intentMap=Intent(this,Map::class.java)
                        finish()
                        if(i.hasChild("location")){
                            startActivity(intentCustomer)
                        }
                        else{
                            startActivity(intentMap)
                        }

                    }
                }
            }
        }
    }









    ///////////////////////////////////////////////////////////////////////////////////////

}