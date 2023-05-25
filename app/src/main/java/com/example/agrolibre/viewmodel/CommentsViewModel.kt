package com.example.agrolibre.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.agrolibre.domain.data.network.Repo
import com.example.agrolibre.model.Comment

class CommentsViewModel : ViewModel() {
    private val repo = Repo()
    fun fetchCommentsData():LiveData<MutableList<Comment>>{
        val mutableData = MutableLiveData<MutableList<Comment>>()
        repo.getCommentsData().observeForever{
            mutableData.value = it
        }
        return mutableData
    }

}