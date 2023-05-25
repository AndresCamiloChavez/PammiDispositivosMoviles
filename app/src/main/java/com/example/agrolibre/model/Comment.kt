package com.example.agrolibre.model

data class Comment(val user: String = "default",
                   val score: String = "5",
                   val content: String = "comment",
                   var userImageUrl: String = "https://cdn-icons-png.flaticon.com/512/149/149071.png"

)
