package com.btl.bookingHotel.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.btl.bookinghotel.databinding.ItemViewPagerBinding
import com.btl.bookinghotel.databinding.ItemViewPagerDetailsBinding
import com.bumptech.glide.Glide

class ViewPagerDetailAdapter(
    private val context: Context,
    private val imageList: List<String>,
) : RecyclerView.Adapter<ViewPagerDetailAdapter.ViewPagerImageViewHolder>() {
    inner class ViewPagerImageViewHolder(val binding: ItemViewPagerDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerImageViewHolder {
        return ViewPagerImageViewHolder(
            ItemViewPagerDetailsBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ViewPagerImageViewHolder, position: Int) {
        val image = imageList[position]
        Glide.with(context)
            .load(image)
            .into(holder.binding.imageView)
    }
}