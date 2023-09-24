package com.example.learn2

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import dataModel.InfoModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.learn2.Models.TeacherAndStudent
import com.example.learn2.databinding.ActivityInfoInputBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage

class InfoInput : AppCompatActivity() {

    private lateinit var binding: ActivityInfoInputBinding

    private var imageUri : Uri? = null

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri= it
        binding.imageView14.setImageURI(imageUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityInfoInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView14.setOnClickListener{
            selectImage.launch("image/*")
        }

        binding.submit.setOnClickListener {
            check()
        }
    }

    private fun check(){

        if (binding.FirstName.text.toString().isEmpty()||binding.LastName.text.toString().isEmpty()
            ||binding.Class.text.toString().isEmpty()||binding.Board.text.toString().isEmpty()
            ||binding.Institute.text.toString().isEmpty()||binding.State.text.toString().isEmpty()
            ||imageUri==null){
            Toast.makeText(this,"Insufficient Information",Toast.LENGTH_SHORT).show()
        }else{
            uploadImage()
        }
    }

    private fun uploadImage(){
        val storageRef= FirebaseStorage.getInstance().getReference("students")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    uploadData(it)
                }.addOnFailureListener{
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadData(imageUrl: Uri?){

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result

            val datas = TeacherAndStudent(
                firstName = binding.FirstName.text.toString(),
                lastName = binding.LastName.text.toString(),
                image = imageUrl.toString(),
                email = FirebaseAuth.getInstance().currentUser!!.email,
                uid = FirebaseAuth.getInstance().currentUser!!.uid,
                fcmToken = token
            )

            val data = InfoModel(
                firstName = binding.FirstName.text.toString(),
                lastName = binding.LastName.text.toString(),
                uclass = binding.Class.text.toString(),
                board = binding.Board.text.toString(),
                institute = binding.Institute.text.toString(),
                state = binding.State.text.toString(),
                image = imageUrl.toString(),
            )
            FirebaseDatabase.getInstance().getReference("TeacherAndStudent")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(datas)
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                    } else {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            FirebaseDatabase.getInstance().getReference("Students")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(data)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this, MainPage::class.java))
                        finish()
                        Toast.makeText(this, "Let's Start Learning", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
        })


    }
}