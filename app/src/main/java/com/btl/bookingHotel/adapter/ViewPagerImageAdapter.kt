package com.btl.bookingHotel.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.btl.bookinghotel.databinding.ItemViewPagerBinding
import com.bumptech.glide.Glide

class ViewPagerImageAdapter(
    private val context: Context,
    private val imageList: List<String>,
) : RecyclerView.Adapter<ViewPagerImageAdapter.ViewPagerImageViewHolder>() {
    inner class ViewPagerImageViewHolder(val binding: ItemViewPagerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerImageViewHolder {
        return ViewPagerImageViewHolder(
            ItemViewPagerBinding.inflate(
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
            .into(holder.binding.mainImg)
    }
}