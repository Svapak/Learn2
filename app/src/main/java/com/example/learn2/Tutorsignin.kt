package com.example.learn2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.learn2.Models.TeacherAndStudent
import com.example.learn2.databinding.ActivityStudentSigninBinding
import com.example.learn2.databinding.ActivityTutorsigninBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

class Tutorsignin : AppCompatActivity() {

    private lateinit var binding: ActivityTutorsigninBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTutorsigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.change.setOnClickListener{
            val intent = Intent(this,TutorSignup::class.java)
            startActivity(intent)
        }
        binding.reset.setOnClickListener{
            val intent= Intent(this,Resete::class.java)
            startActivity(intent)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient= GoogleSignIn.getClient(this,gso)

        binding.google.setOnClickListener(){
            signInGoogle()
        }

        binding.login.setOnClickListener{
            val loading = LoadingDialog(this)
            loading.startLoading()
            val handler= Handler()
            handler.postDelayed(object :Runnable{
                override fun run() {
                    loading.isDismiss()
                }
            },5000)
            val email=binding.email.text.toString()
            val pass=binding.password.text.toString()


            if(email.isNotEmpty() && pass.isNotEmpty()){

                firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener{
                    var token: String? = null
                    if(it.isSuccessful){
                        val intent= Intent(this,MainPageTeacher::class.java)
                        FirebaseMessaging.getInstance().token.addOnCompleteListener(
                            OnCompleteListener {task->
                                if(!task.isSuccessful){
                                    return@OnCompleteListener
                                }
                                token= task.result
                            })

                        var Firstname: String?=null
                        var Lastname: String?=null
                        var Image: String?=null
                        var Email: String?=null
                        var Uid: String?=null

                        FirebaseDatabase.getInstance().getReference("TeacherAndStudent").child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .get().addOnSuccessListener {
                                if(it.exists()) {
                                    val dataguide= it.getValue(TeacherAndStudent::class.java)
                                    Firstname= dataguide!!.firstName.toString()
                                    Lastname= dataguide.lastName.toString()
                                    Image= dataguide.image.toString()
                                    Email= dataguide.email.toString()
                                    Uid= dataguide.uid!!.toString()

                                    val data= TeacherAndStudent(
                                        fcmToken = token,
                                        firstName = Firstname,
                                        lastName = Lastname,
                                        image = Image,
                                        uid = Uid,
                                        email = Email,
                                    )

                                    FirebaseDatabase.getInstance().getReference("TeacherAndStudent").child(FirebaseAuth.getInstance().currentUser!!.uid).removeValue()

                                    FirebaseDatabase.getInstance().getReference("TeacherAndStudent")
                                        .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(data).addOnCompleteListener{
                                            if(it.isSuccessful){
                                                Toast.makeText(this,"",Toast.LENGTH_SHORT).show()
                                            }else{
                                                Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_SHORT).show()
                                            }
                                        }


                                }
                            }

                        startActivity(intent)
                    }else{
                        Toast.makeText(this,"Password Invalid", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Insufficient Information", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if(result.resultCode== Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>){
        if(task.isSuccessful){
            val account: GoogleSignInAccount? = task.result
            if(account!=null){
                updateUI(account)
            }
        }
        else{
            Toast.makeText(this,task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                val intent= Intent(this,MainPageTeacher::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

}