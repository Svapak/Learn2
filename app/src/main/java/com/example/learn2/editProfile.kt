package com.example.learn2

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.learn2.databinding.ActivityEditProfileBinding
import com.example.learn2.ui.profile.ProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dataModel.ImageData
import dataModel.InfoModel

class editProfile : AppCompatActivity() {

    private lateinit var binding:ActivityEditProfileBinding

    private var imageUri: Uri?=null

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it
        binding.timage.setImageURI(imageUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tname.setOnClickListener{
            selectImage.launch("image/*")
        }
        binding.save.setOnClickListener{
            validateData()
        }
    }

    private fun validateData(){
        if(binding.tname.text.toString().isEmpty()||
                binding.ttname.text.toString().isEmpty()
            ||binding.tboard.text.toString().isEmpty()
            ||binding.tclass.text.toString().isEmpty()
            ||binding.tinstitute.text.toString().isEmpty()
            ||binding.tstate.text.toString().isEmpty()
            ||imageUri==null){
            Toast.makeText(this,"Information Insufficient",Toast.LENGTH_SHORT).show()
        }else{
            uploadImage()
        }
    }
    private fun uploadImage(){
        val storageRef = FirebaseStorage.getInstance().getReference("Profile Image")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    storeData(it)
                }.addOnFailureListener{
                    Toast.makeText(this,"Error Ocurred",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                Toast.makeText(this,"Error Occured",Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(imageUrl: Uri?){
        val data = InfoModel(
            image = imageUrl.toString(),
            firstName = binding.tname.text.toString(),
            lastName = binding.ttname.text.toString(),
            uclass = binding.tclass.text.toString(),
            board = binding.tboard.text.toString(),
            state = binding.tboard.text.toString(),
            institute = binding.tinstitute.text.toString(),
        )

        FirebaseDatabase.getInstance().getReference("Students")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(data).addOnCompleteListener{
                if(it.isSuccessful){
                    startActivity(Intent(this,ProfileFragment::class.java))
                    finish()
                }else{
                    Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
                }
            }
    }
}