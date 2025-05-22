package com.btl.bookingHotel.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.btl.bookingHotel.utils.Constants
import com.btl.bookinghotel.databinding.ItemImageBinding
import com.bumptech.glide.Glide

class CommentImageAdapter(
    private val context: Context,
    private val images: List<String>
) : RecyclerView.Adapter<CommentImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val url =
            Constants.BASE_URL + images[position]
        Glide.with(context).load(url).into(holder.binding.imageView)
    }
}
