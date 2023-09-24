package com.example.learn2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.learn2.Adapter.MessageAdapter
import com.example.learn2.Models.TeacherAndStudent
import com.example.learn2.databinding.ActivityChatBinding
import com.example.learn2.notification.ApiUtilities
import com.example.learn2.notification.NotificationData
import com.example.learn2.notification.PushNotification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dataModel.MessageModel
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.security.auth.callback.Callback

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getData(intent.getStringExtra("chat_id"))

        varifyChatId()

        binding.imageView8.setOnClickListener{
            if(binding.message.text!!.isEmpty()){
                Toast.makeText(this,"Please enter your text",Toast.LENGTH_SHORT).show()
            }else{
                storeData(binding.message.text.toString())
            }
        }
    }

    private var senderId: String? = null
    private var chatId: String? = null
    private var recieverId: String? = null

    private fun varifyChatId() {

        senderId=FirebaseAuth.getInstance().currentUser!!.uid
        recieverId = intent.getStringExtra("userId")

        chatId=senderId+recieverId
        val reverseChatId = recieverId+senderId

        val reference = FirebaseDatabase.getInstance().getReference("chats")

        reference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(chatId!!)){
                    getData(chatId)
                }else if(snapshot.hasChild(reverseChatId)){
                    chatId=reverseChatId
                    getData(chatId)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity,"Something went wrong",Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun getData(chatId: String?){
        FirebaseDatabase.getInstance().getReference("chats").child(chatId!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = arrayListOf<MessageModel>()

                    for (show in snapshot.children){
                        list.add(show.getValue(MessageModel::class.java)!!)
                    }

                    binding.recyclerView2.adapter = MessageAdapter(this@ChatActivity,list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity,"jjj",Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun storeData( msg: String) {

        val currentDate:String =SimpleDateFormat("dd-MM-yyyy",Locale.getDefault()).format(Date())
        val currentTime:String = SimpleDateFormat("HH:mm a",Locale.getDefault()).format(Date())

        val map= hashMapOf<String,String>()
        map["message"]=msg
        map["senderId"]=senderId!!
        map["currentTime"]=currentTime
        map["currentDate"]=currentDate

        val reference = FirebaseDatabase.getInstance().getReference("chats").child(chatId!!)

        reference.child(reference.push().key!!)
            .setValue(map).addOnCompleteListener{
                if(it.isSuccessful){
                    binding.message.text= null

                    sendNotification(msg)

                    Toast.makeText(this,"Message Sent",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun sendNotification(msg: String) {
        FirebaseDatabase.getInstance().getReference("TeacherAndStudent")
            .child(recieverId!!).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            val data =snapshot.getValue(TeacherAndStudent::class.java)

//                            val noitificationData=
//                                PushNotification(NotificationData("New Messages",msg),
//                                    data!!.fcmToken
//                                )
//
//                            ApiUtilities.getInstance().sendNotification(
//                                noitificationData
//                            ).enqueue(object: retrofit2.Callback<PushNotification>{
//                                override fun onResponse(
//                                    call: Call<PushNotification>,
//                                    response: Response<PushNotification>
//                                ) {
//                                    Toast.makeText(this@ChatActivity,"notification",Toast.LENGTH_SHORT).show()
//                                }
//
//                                override fun onFailure(call: Call<PushNotification>, t: Throwable) {
//                                    Toast.makeText(this@ChatActivity,"Something went wrong",Toast.LENGTH_SHORT).show()
//                                }
//
//                            })
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@ChatActivity,error.message, Toast.LENGTH_SHORT).show()
                    }

                }
            )
    }
}
