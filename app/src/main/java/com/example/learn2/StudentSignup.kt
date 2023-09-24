package com.example.learn2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.learn2.databinding.ActivityStudentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

public class StudentSignup : AppCompatActivity() {

    private lateinit var binding: ActivityStudentSignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityStudentSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.change.setOnClickListener{
            val intent =Intent(this,StudentSIgnin::class.java)
            startActivity(intent)
        }

        binding.signup.setOnClickListener{
            val loading=LoadingDialog(this)
            loading.startLoading()
            val handler=Handler()
            handler.postDelayed(object :Runnable{
                override fun run() {
                    loading.isDismiss()
                }
            },5000)
            val email= binding.email.text.toString()
            val name= binding.name.text.toString()
            val pass= binding.password.text.toString()
            val confpass=binding.confirmpassword.text.toString()

            if(email.isNotEmpty() && pass.isNotEmpty() && confpass.isNotEmpty() && name.isNotEmpty()){
                if(pass == confpass){

                    firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener{

                        if(it.isSuccessful){
                            val currentuser : FirebaseUser? = firebaseAuth.currentUser
                            currentuser?.let{
                                val storageRef=FirebaseDatabase.getInstance().getReference("name").child(FirebaseAuth.getInstance().currentUser!!.uid)

                                storageRef.child(name)
                            }
                            val intent =Intent(this,InfoInput::class.java)
                            startActivity(intent)
                        }
                        else{
                            Toast.makeText(this,it.exception.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    Toast.makeText(this,"Password doesn't match",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Insufficient information",Toast.LENGTH_SHORT).show()
            }
        }
    }
}