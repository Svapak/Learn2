package com.example.learn2.Repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import dataModel.TeacherData

class ItemRepository {

    private val databaseReference: DatabaseReference=
        FirebaseDatabase.getInstance().getReference("Teachers")

    public val uid:String?=null

    @Volatile
    private var INSTANCE: ItemRepository?=null

    fun getInstance(): ItemRepository{
        return INSTANCE ?: synchronized(this){

            val instance = ItemRepository()
            INSTANCE=instance
            instance
        }
    }

    fun loadItems(itemList:MutableLiveData<List<TeacherData>>){

        databaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                try{
                    val _itemList: List<TeacherData> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(TeacherData::class.java)!!
                    }
                    itemList.postValue(_itemList)
                }catch (e:java.lang.Exception){

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}