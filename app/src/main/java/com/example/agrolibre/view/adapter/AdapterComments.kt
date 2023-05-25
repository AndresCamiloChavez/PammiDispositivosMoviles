package com.example.agrolibre.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agrolibre.R
import com.example.agrolibre.model.Comment
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text

class AdapterComments: RecyclerView.Adapter<AdapterComments.CommentsViewHolder>() {

    private var listComments = mutableListOf<Comment>()


    fun setListComments(data: MutableList<Comment>){
        listComments = data
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent,false)
        return CommentsViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bindView(listComments[position])
    }

    override fun getItemCount(): Int = listComments.size
    inner class CommentsViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindView(comment: Comment){
            val img = itemView.findViewById<CircleImageView>(R.id.imgUserComment)
            Glide.with(itemView.context).load(comment.userImageUrl).into(img)
            itemView.findViewById<TextView>(R.id.tvItemCommentUser).text = comment.user
            itemView.findViewById<RatingBar>(R.id.ratingBarCommentItem).rating = comment.score.toFloat()
            itemView.findViewById<TextView>(R.id.tvItemCommentsContent).text = comment.content

        }
    }

}