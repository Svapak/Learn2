package com.example.learn2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.learn2.databinding.ActivityResetBinding
import com.example.learn2.databinding.ActivityReseteBinding
import com.google.firebase.auth.FirebaseAuth

class Resete : AppCompatActivity() {

    private lateinit var binding: ActivityReseteBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityReseteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()

        binding.reset.setOnClickListener{
            val email= binding.email.text.toString()
            firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
                Toast.makeText(this,"Please check your Email", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener{
                    Toast.makeText(this,"Error Occured", Toast.LENGTH_SHORT).show()
                }
        }


    }
}