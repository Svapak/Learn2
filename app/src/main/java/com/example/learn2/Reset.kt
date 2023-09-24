package com.example.learn2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.learn2.databinding.ActivityResetBinding
import com.google.firebase.auth.FirebaseAuth

class Reset : AppCompatActivity() {

    private lateinit var binding: ActivityResetBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityResetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()

        binding.reset.setOnClickListener{
            val email= binding.email.text.toString()
            firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
                Toast.makeText(this,"Please check your Email",Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener{
                    Toast.makeText(this,"Error Occured",Toast.LENGTH_SHORT).show()
                }
        }


    }
}