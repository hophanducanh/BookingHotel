package com.btl.bookingHotel.adapter

import com.btl.bookinghotel.databinding.ItemCommentBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.btl.bookingHotel.model.Comment
import com.bumptech.glide.Glide

class CommentAdapter(private val context: Context, private var comments: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    inner class CommentViewHolder(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(comment.created_at)

        val outputFormat = SimpleDateFormat("EEE, d 'thg' M, yyyy", Locale("vi", "VN"))
        val formattedDate = outputFormat.format(date ?: Date())

        val displayDate = formattedDate.replaceFirstChar { it.uppercase() }

        holder.binding.tvDate.text = displayDate

        holder.binding.tvRating.text = comment.rating_point.toString()

        holder.binding.tvUserName.text = comment.user.user_name
        holder.binding.tvComment.text = comment.comment

        Glide.with(context)
            .load("https://scaling-space-telegram-pqq5wq6pqg6c964q-5000.app.github.dev/" + comment.user.avatar_url)
            .into(holder.binding.imgAvatar)

        val imageList = comment.images
        if (imageList.isNotEmpty()) {
            val adapter = CommentImageAdapter(context, imageList)
            holder.binding.listItem.adapter = adapter
            holder.binding.listItem.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<Comment>) {
        comments = newList
        notifyDataSetChanged()
    }
}