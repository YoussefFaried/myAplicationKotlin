package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapplication.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var Binding:ActivitySignUpBinding
    private lateinit var AuthFireBase:FirebaseAuth
    private lateinit var dataBase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ViewBinding
        Binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(Binding.root)
        //Firebase Authentication
        AuthFireBase= FirebaseAuth.getInstance()






        //class User
        data class User(val firstname:String?=null ,val lastname:String?=null,val age:String?=null,val phone:String?=null)
//////////////////////////////////////////////////////////////////////////////////////////////////////////

        //OnClick Listeners
        Binding.SignInLabel.setOnClickListener {

            Binding.progressBar2.visibility= View.VISIBLE

            val intent1= Intent(this,SignIn::class.java)
            startActivity(intent1)
        }

        Binding.SignUpButton.setOnClickListener{
            Binding.progressBar2.visibility= View.VISIBLE
            Binding.SignUpButton.visibility=View.INVISIBLE
            Binding.SignInLabel.visibility=View.INVISIBLE

            val Email=Binding.SignUpEmail.text.toString()
            val Password1=Binding.SignUpPassword1.text.toString()
            val Password2=Binding.SignUpPassword2.text.toString()

            val firstName=Binding.SignUpFirstName.text.toString()
            val lastName=Binding.SignUpLastName.text.toString()
            val age=Binding.SignUpAge.text.toString()
            val phone=Binding.SignUpPhone.text.toString()
            val customer=Binding.Customer.isChecked()
            val seller=Binding.Seller.isChecked()

            if(Email.isNotEmpty() && Password1.isNotEmpty() && Password2.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty() && age.isNotEmpty() &&phone.isNotEmpty() && (customer||seller)){
                if(! Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    Toast.makeText(this,"Inavalid Email Address",Toast.LENGTH_SHORT).show()
                    Binding.progressBar2.visibility= View.INVISIBLE
                    Binding.SignUpButton.visibility=View.VISIBLE
                    Binding.SignInLabel.visibility=View.VISIBLE

                }
                else if(Password1!=Password2){
                    Toast.makeText(this,"Password is not matching",Toast.LENGTH_SHORT).show()
                    Binding.progressBar2.visibility= View.INVISIBLE
                    Binding.SignUpButton.visibility=View.VISIBLE
                    Binding.SignInLabel.visibility=View.VISIBLE

                }
                else{

                    AuthFireBase.createUserWithEmailAndPassword(Email,Password1).addOnCompleteListener() {
                        if(it.isSuccessful){
                            if(customer){
                                dataBase=FirebaseDatabase.getInstance().getReference("Customers")
                                Log.d("haha","hahaha")
                            }
                            else{
                                dataBase= FirebaseDatabase.getInstance().getReference("Sellers")
                            }
                            val intent2=Intent(this,SignIn::class.java)

                            val user=User(firstName,lastName,age,phone)
                            val iD=(AuthFireBase.uid).toString()
                            dataBase.child(iD).child("personal info").setValue(user)

                            finish()

                            startActivity(intent2)

                        }
                        else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                            Binding.progressBar2.visibility= View.INVISIBLE
                            Binding.SignUpButton.visibility=View.VISIBLE
                            Binding.SignInLabel.visibility=View.VISIBLE

                        }
                    }
                }

            }
            else{
                Toast.makeText(this,"Some fields are empty",Toast.LENGTH_SHORT).show()
                Binding.progressBar2.visibility= View.INVISIBLE
                Binding.SignUpButton.visibility=View.VISIBLE
                Binding.SignInLabel.visibility=View.VISIBLE

            }



        }
    }
}