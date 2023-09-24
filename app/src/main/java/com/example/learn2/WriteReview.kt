package com.example.learn2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.learn2.bottomnavigationfragments.tutors
import com.example.learn2.databinding.ActivityWriteReviewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dataModel.InfoModel
import dataModel.review_model
import java.util.Calendar

class WriteReview : AppCompatActivity() {

    private lateinit var binding:ActivityWriteReviewBinding
    var tutoruid:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityWriteReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val images=intent.getStringExtra("image")
        val names=intent.getStringExtra("name")
        val emails=intent.getStringExtra("email")

        binding.textView12.setText(names)
        binding.textView14.setText(emails)
        Glide.with(this).load(images).into(binding.imageView6)

        binding.button.setOnClickListener{
            validateData()
        }

        tutoruid = intent.getStringExtra("userId")
    }

    private fun validateData() {
        if(binding.review.text.toString().isEmpty()||binding.rate.toString().isEmpty()){
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
        }
        else{
            uploadData()
        }
    }

    private fun uploadData() {

        var rating:String?=null

        val  ttt=binding.rate

        ttt.setOnRatingBarChangeListener{tttt,fl,b->
            rating=tttt.rating.toString()
        }

        var names:String?=null
        var images:String?=null

        FirebaseDatabase.getInstance().getReference("Students").child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val infoModel = snapshot.getValue(InfoModel::class.java)
                    infoModel?.let { data->
                        names=data.firstName.toString()+" "+data.lastName.toString()
                        images=data.image.toString()

                        Log.d("qw",names!!)
                        //Log.d("qw",rating!!)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


//        val data = review_model(
//            rating = rating!!,
//            body = binding.review.text.toString(),
//            name = names!!,
//            image = images!!,
//            time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString()+":"+Calendar.getInstance().get(Calendar.MINUTE)
//        )
//
//
//        FirebaseDatabase.getInstance().getReference("Reviews")
//            .child(tutoruid!!).child(FirebaseAuth.getInstance().currentUser!!.uid)
//            .setValue(data).addOnCompleteListener{
//
//                if(it.isSuccessful){
//                    startActivity(Intent(this,MainPage::class.java))
//                }else{
//                    Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
//                }
//            }
    }


}