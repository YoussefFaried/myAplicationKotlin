package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapplication.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {

    private lateinit var Binding:ActivitySignUpBinding
    private lateinit var AuthFireBase:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ViewBinding
        Binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(Binding.root)
        //Firebase Authentication
        AuthFireBase= FirebaseAuth.getInstance()
//////////////////////////////////////////////////////////////////////////////////////////////////////////

        //OnClick Listeners
        Binding.SignInLabel.setOnClickListener {

            Binding.progressBar2.visibility= View.VISIBLE

            val intent1= Intent(this,SignIn::class.java)
            startActivity(intent1)
        }

        Binding.SignUpButton.setOnClickListener{
            Binding.progressBar2.visibility= View.VISIBLE

            val Email=Binding.SignUpEmail.text.toString()
            val Password1=Binding.SignUpPassword1.text.toString()
            val Password2=Binding.SignUpPassword2.text.toString()

            if(Email.isNotEmpty() && Password1.isNotEmpty() && Password2.isNotEmpty()){
                if(! Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    Toast.makeText(this,"Inavalid Email Address",Toast.LENGTH_SHORT).show()
                    Binding.progressBar2.visibility= View.INVISIBLE

                }
                else if(Password1!=Password2){
                    Toast.makeText(this,"Password is not matching",Toast.LENGTH_SHORT).show()
                    Binding.progressBar2.visibility= View.INVISIBLE

                }
                else{
                    AuthFireBase.createUserWithEmailAndPassword(Email,Password1).addOnCompleteListener() {
                        if(it.isSuccessful){
                            val intent2=Intent(this,SignIn::class.java)
                            startActivity(intent2)
                        }
                        else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                            Binding.progressBar2.visibility= View.INVISIBLE

                        }
                    }
                }

            }
            else{
                Toast.makeText(this,"Some fields are empty",Toast.LENGTH_SHORT).show()
                Binding.progressBar2.visibility= View.INVISIBLE

            }

        }
    }
}