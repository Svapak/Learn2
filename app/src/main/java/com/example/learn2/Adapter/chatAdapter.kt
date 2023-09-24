package com.example.learn2.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.learn2.ChatActivity
import com.example.learn2.Models.TeacherAndStudent
import com.example.learn2.databinding.UserInteractedLayoutBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import dataModel.TeacherData

class chatAdapter(val context: Context, val list: ArrayList<String>, val chatKey: List<String>) : RecyclerView.Adapter<chatAdapter.ChatViewHolder>(){

    inner class ChatViewHolder(val binding : UserInteractedLayoutBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(UserInteractedLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        FirebaseDatabase.getInstance().getReference("TeacherAndStudent")
            .child(list[position]).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            val data =snapshot.getValue(TeacherAndStudent::class.java)
                            Glide.with(context).load(data!!.image).into(holder.binding.userImage)
                            holder.binding.userName.text = data.firstName+" "+data.lastName
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context,error.message, Toast.LENGTH_SHORT).show()
                    }

                }
            )

        holder.itemView.setOnClickListener{
            val intent = Intent(context,ChatActivity::class.java)
            intent.putExtra("userId",list[position])
            context.startActivity(intent)
        }
    }
}