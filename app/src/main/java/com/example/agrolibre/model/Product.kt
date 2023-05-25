package com.example.agrolibre.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(val imageUrl: String ="url",
                   val name: String = "nombre",
                   val price: String = "1000",
                   val description: String = "descripcion"
) : Parcelable