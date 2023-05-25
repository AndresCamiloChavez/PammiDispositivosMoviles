package com.example.agrolibre.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.agrolibre.domain.data.network.Repo
import com.example.agrolibre.model.Product

class ProductsViewModel : ViewModel() {
    private val repo = Repo()
    fun fetchProductsData():LiveData<MutableList<Product>>{
        val  mutableData = MutableLiveData<MutableList<Product>>()
        repo.getProductsData().observeForever{
            mutableData.value = it
        }
        return mutableData
    }
}