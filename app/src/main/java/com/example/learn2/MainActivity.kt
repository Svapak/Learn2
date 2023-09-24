package com.example.learn2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learn2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.stuView2.setOnClickListener{
            val intent= Intent(this,StudentSIgnin::class.java)
            startActivity(intent)
        }

        binding.tutorView.setOnClickListener {
            val intent=Intent(this,Tutorsignin::class.java)
            startActivity(intent)
        }

    }

}