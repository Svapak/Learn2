package com.example.learn2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.learn2.databinding.ActivityTutorDesBinding

class TutorDes : AppCompatActivity() {
    private lateinit var binding:ActivityTutorDesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTutorDesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val tnames: TextView = findViewById(R.id.textView12)
        val temails: TextView = findViewById(R.id.textView14)
        val timages: ImageView = findViewById(R.id.imageView6)
        val tratings: TextView = findViewById(R.id.textView59)
        val tsubscribers: TextView = findViewById(R.id.textView57)
        val thired: TextView = findViewById(R.id.textView5)
        val tmonthlyview: TextView = findViewById(R.id.textView19)
        val tdoubtfee: TextView = findViewById(R.id.textView21)
        val tadress: TextView = findViewById(R.id.textView23)
        val tstate: TextView = findViewById(R.id.textView25)
        val tqualification: TextView = findViewById(R.id.textView9)
        val tdayandtime: TextView = findViewById(R.id.textView16)

        val names = intent.getStringExtra("firstname") +" " +intent.getStringExtra("lastname")
        val images = intent.getStringExtra("image")
        val emails = intent.getStringExtra("email")
        val ratings = intent.getStringExtra("ratings")
        val subscribers = intent.getStringExtra("students")
        val hired = intent.getStringExtra("hiringstatus")
        val monthlyfee = intent.getStringExtra("monthlyfee")
        val doubtfee = intent.getStringExtra("doubtfee")
        val adress = intent.getStringExtra("adress")
        val states = intent.getStringExtra("state")
        val qualification = intent.getStringExtra("qualifications")
        val dayandtime = intent.getStringExtra("dayandtime")
        val uids=intent.getStringExtra("uid")

        tnames.text=names
        temails.text=emails
        tratings.text=ratings
        tsubscribers.text=subscribers
        thired.text=hired
        tmonthlyview.text=monthlyfee
        tdoubtfee.text=doubtfee
        tadress.text=adress
        tstate.text=states
        tqualification.text=qualification
        tdayandtime.text=dayandtime
        Glide.with(this).load(images).into(timages)

        binding.chat.setOnClickListener{
            val intent= Intent(this,ChatActivity::class.java)
            intent.putExtra("userId",uids)
            startActivity(intent)
        }

        binding.write.setOnClickListener{
            val intent= Intent(this,WriteReview::class.java)
            intent.putExtra("userId",uids)
            intent.putExtra("image",images)
            intent.putExtra("email",emails)
            intent.putExtra("name",names)
            startActivity(intent)
        }

    }
}