package com.example.learn2

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.learn2.Models.TeacherAndStudent
import com.example.learn2.databinding.ActivityTeacherInfoInputBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dataModel.TeacherData

class TeacherInfoInput : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherInfoInputBinding

    private var imageUri: Uri?=null
    var amount:String?=null
    var aamount:String?=null

    private val selectImage= registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri =it
        binding.imageView14.setImageURI(imageUri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTeacherInfoInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val items = listOf("1000","2000","3000","4000","5000","Negotiable")
        val autoComplete: AutoCompleteTextView=findViewById(R.id.auto_complete_txt)
        val adapter= ArrayAdapter(this,R.layout.item_list,items)

        autoComplete.setAdapter(adapter)

        autoComplete.onItemClickListener= AdapterView.OnItemClickListener { parent, view, position, id ->
            val itemSelected = parent.getItemAtPosition(position)
            amount=itemSelected.toString()
        }
        val iitems = listOf("50","100","150","200","250","300")
        val aautoComplete: AutoCompleteTextView=findViewById(R.id.doubt_fee)
        val aadapter= ArrayAdapter(this,R.layout.item_list,iitems)

        aautoComplete.setAdapter(aadapter)

        aautoComplete.onItemClickListener= AdapterView.OnItemClickListener { parent, view, position, id ->
            val itemSelected = parent.getItemAtPosition(position)
            aamount=itemSelected.toString()
        }
        binding.imageView14.setOnClickListener{
            selectImage.launch("image/*")
        }

        binding.submit.setOnClickListener {
            check()
        }
    }

    private fun check(){

        if (binding.FirstName.text.toString().isEmpty()||binding.LastName.text.toString().isEmpty()
            ||binding.Adress.text.toString().isEmpty()||binding.Qualification.text.toString().isEmpty()
            ||binding.pincode.text.toString().isEmpty()||binding.State.text.toString().isEmpty()
            ||binding.Phoneno.text.toString().isEmpty()||imageUri==null|| amount==null || aamount==null
            ||binding.dayandtime.text.toString().isEmpty()){
            Toast.makeText(this,"Insufficient Information", Toast.LENGTH_SHORT).show()
        }else{
            uploadImage()
        }
    }

    private fun uploadImage(){
        val storageRef = FirebaseStorage.getInstance().getReference("teacher")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    uploadData(it)
                }.addOnFailureListener{
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener{
                Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadData(imageUrl:Uri?) {

        val datas= TeacherAndStudent(
            firstName = binding.FirstName.text.toString(),
            lastName = binding.LastName.text.toString(),
            email = FirebaseAuth.getInstance().currentUser!!.email,
            uid=FirebaseAuth.getInstance().currentUser!!.uid,
            image = imageUrl.toString(),

        )

        val data = TeacherData(
            firstName = binding.FirstName.text.toString(),
            lastName = binding.LastName.text.toString(),
            pincode = binding.pincode.text.toString(),
            qualifications = binding.Qualification.text.toString(),
            adress = binding.Adress.text.toString(),
            state = binding.State.text.toString(),
            phoneno = binding.Phoneno.text.toString(),
            image = imageUrl.toString(),
            monthlyfee = amount,
            doubtfee = aamount,
            dayandtime = binding.dayandtime.text.toString(),
            hiringstatus = "Available",
            averagerating = "Not rated yet",
            studentssubscribed = "0",
            email = FirebaseAuth.getInstance().currentUser!!.email,
            uid=FirebaseAuth.getInstance().currentUser!!.uid,
        )
        FirebaseDatabase.getInstance().getReference("TeacherAndStudent")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(datas).addOnCompleteListener{
                if (it.isSuccessful){

                }else{
                    Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        FirebaseDatabase.getInstance().getReference("Teachers")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(data).addOnCompleteListener{
                if (it.isSuccessful){
                    startActivity(Intent(this,MainPageTeacher::class.java))
                    finish()
                    Toast.makeText(this,"Let's Start Learning", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }


    }
}