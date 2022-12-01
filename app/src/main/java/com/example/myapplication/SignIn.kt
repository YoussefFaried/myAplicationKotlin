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


class SignIn : AppCompatActivity() {


    private lateinit var Binding:ActivitySignInBinding
    private lateinit var AuthFireBase:FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Binding= ActivitySignInBinding.inflate(layoutInflater)
        setContentView(Binding.root)

        AuthFireBase= FirebaseAuth.getInstance()

        Binding.SignUpLabel.setOnClickListener {
            Binding.progressBar.visibility= VISIBLE
            val intent1 = Intent(this, SignUp::class.java)
            startActivity(intent1)

        }
        Binding.SignInButton.setOnClickListener{
            Binding.progressBar.visibility= VISIBLE

            val Email=Binding.SignInEmail.text.toString()
            val Password=Binding.SignInPassword.text.toString()
            if(Email.isNotEmpty() && Password.isNotEmpty() ){
                AuthFireBase.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(){
                    if(it.isSuccessful){
                        val intent2=Intent(this,CustomerActivity::class.java)
                        startActivity(intent2 )
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
            val intent3=Intent(this,CustomerActivity::class.java)
            startActivity(intent3)
        }
    }
}