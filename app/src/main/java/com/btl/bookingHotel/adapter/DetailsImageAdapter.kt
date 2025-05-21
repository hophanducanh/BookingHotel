package com.btl.bookingHotel.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.btl.bookinghotel.databinding.ItemImageDetailsBinding
import com.bumptech.glide.Glide

class DetailsImageAdapter(
    private val context: Context,
    private val imageList: List<String>,
    private val onImageClick: (Int) -> Unit
) : RecyclerView.Adapter<DetailsImageAdapter.DetailsImageViewHolder>() {
    inner class DetailsImageViewHolder(val binding: ItemImageDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsImageViewHolder {
        return DetailsImageViewHolder(
            ItemImageDetailsBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return if (imageList.size > 4) 4 else imageList.size
    }

    override fun onBindViewHolder(holder: DetailsImageViewHolder, position: Int) {
        Glide.with(context)
            .load(imageList[position])
            .into(holder.binding.imageView)

        if (position == 3 && imageList.size > 4) {
            val moreCount = imageList.size - 4
            holder.binding.tvMore.visibility = View.VISIBLE
            holder.binding.tvMore.text = "+$moreCount"
        } else {
            holder.binding.tvMore.visibility = View.GONE
        }

        holder.binding.root.setOnClickListener {
            onImageClick(position)
        }
    }
}