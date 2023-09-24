package com.example.learn2.Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.learn2.Repository.ItemRepository
import dataModel.TeacherData

class TutorViewModel : ViewModel(){

    private val repository : ItemRepository
    private val _allItems = MutableLiveData<List<TeacherData>>()
    val allItems :LiveData<List<TeacherData>> = _allItems

    init {
        repository=ItemRepository().getInstance()
        repository.loadItems(_allItems)
    }
}