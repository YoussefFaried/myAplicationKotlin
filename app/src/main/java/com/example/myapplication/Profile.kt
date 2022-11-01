package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Toast
import com.example.myapplication.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Profile : AppCompatActivity() {
    private lateinit var Binding:ActivityProfileBinding
    private lateinit var AuthFireBase:FirebaseAuth
    private lateinit var dataBase:DatabaseReference
    private lateinit var CustomerDB:DatabaseReference
    private lateinit var SellerDB:DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /////initializations
        Binding= ActivityProfileBinding.inflate(layoutInflater)
        setContentView(Binding.root)
        AuthFireBase= FirebaseAuth.getInstance()


        ///////To be used later
        val ID=(AuthFireBase.uid).toString()
        val intentBack=Intent(this,MainActivity::class.java)
        data class User(val firstname:String?=null ,val lastname:String?=null,val age:String?=null,val phone:String?=null)
        //////



        //////retrieving data from database
        //////the user personal info will be found if he has updated his profile once at least after signing up

        CustomerDB=FirebaseDatabase.getInstance().getReference("Customers")
        SellerDB=FirebaseDatabase.getInstance().getReference("Sellers")
        var found=false
        CustomerDB.get().addOnSuccessListener {
            for(i in it.children){
                if((i.key).toString()==ID){
                    Binding.Customer.isChecked=true
                    Binding.Customer.isEnabled=false
                    Binding.Seller.isEnabled=false
                    Binding.FirstName.setText(i.child("personal info").child("firstname").getValue().toString())
                    Binding.LastName.setText(i.child("personal info").child("lastname").getValue().toString())
                    Binding.Age.setText(i.child("personal info").child("age").getValue().toString())
                    Binding.Phone2.setText(i.child("personal info").child("phone").getValue().toString())
                    found=true

                }
            }
        }
        if(!found){
            SellerDB.get().addOnSuccessListener {
                for(i in it.children){
                    if((i.key).toString()==ID){
                        Binding.Seller.isChecked=true
                        Binding.Customer.isEnabled=false
                        Binding.Seller.isEnabled=false
                        Binding.FirstName.setText(i.child("personal info").child("firstname").getValue().toString())
                        Binding.LastName.setText(i.child("personal info").child("lastname").getValue().toString())
                        Binding.Age.setText(i.child("personal info").child("age").getValue().toString())
                        Binding.Phone2.setText(i.child("personal info").child("phone").getValue().toString())

                    }
                }
            }
        }









        ///////eventlisteners
        Binding.button2.setOnClickListener {
            startActivity(intentBack)
        }
        Binding.button.setOnClickListener {
            Binding.progressBar3.visibility= View.VISIBLE
            Binding.button2.isEnabled=false


            val firstName=Binding.FirstName.text.toString()
            val lastName=Binding.LastName.text.toString()
            val age=Binding.Age.text.toString()
            val phone=Binding.Phone2.text.toString()
            val customer=Binding.Customer.isChecked()
            val seller=Binding.Seller.isChecked()

            if(firstName.isNotEmpty() && lastName.isNotEmpty() && age.isNotEmpty() && phone.isNotEmpty() && (customer||seller)){
                if(customer){
                    dataBase=FirebaseDatabase.getInstance().getReference("Customers")
                }
                else{
                    dataBase=FirebaseDatabase.getInstance().getReference("Sellers")
                }

                val user=User(firstName,lastName,age,phone)
                dataBase.child(ID).child("personal info").setValue(user)

                //////this is another way to write every node alone
                /*
                dataBase.child(ID).child("first name").setValue(firstName)
                dataBase.child(ID).child("last name").setValue(lastName)
                dataBase.child(ID).child("age").setValue(age)
                dataBase.child(ID).child("phone").setValue(phone)*/

                startActivity(intentBack)

            }
            else{
                Toast.makeText(this,"Some Fields Are Empty",Toast.LENGTH_SHORT).show()
                Binding.progressBar3.visibility= View.INVISIBLE
                Binding.button2.isEnabled=true
            }










        }



    }
}