package com.example.learn2.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.learn2.R
import com.example.learn2.bottomnavigationfragments.tutors
import dataModel.TeacherData

class TutorAdapter (val context: Context,private  val itemList: ArrayList<TeacherData> = ArrayList<TeacherData>(),private val onItemClickListener: tutors):RecyclerView.Adapter<TutorAdapter.MyViewHolder>(){

    private val names = arrayListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_layout,
            parent,false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = itemList[position]

        holder.name.text=currentitem.firstName+" "+currentitem.lastName
        holder.availability.text=currentitem.hiringstatus
        Glide.with(context).load(currentitem.image).into(holder.imageView)

        holder.itemView.setOnClickListener{
            onItemClickListener.onItemItemClicked(position)
        }

        currentitem.firstName?.let { names.add(it) }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun updateItemList(itemList: List<TeacherData>){
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val name : TextView= itemView.findViewById(R.id.name)
        val availability : TextView= itemView.findViewById(R.id.availability)
        val imageView : ImageView = itemView.findViewById(R.id.imageView)
    }

}