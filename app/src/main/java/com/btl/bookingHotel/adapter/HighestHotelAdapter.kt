package com.btl.bookingHotel.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.btl.bookingHotel.model.HotelData
import com.btl.bookinghotel.R
import com.btl.bookinghotel.databinding.ItemHighestHotelBinding
import com.bumptech.glide.Glide
import java.text.DecimalFormat

class HighestHotelAdapter(private val context: Context, private var hotels: List<HotelData>) :
    RecyclerView.Adapter<HighestHotelAdapter.HighestHotelViewHolder>() {

    private var listener: ((HotelData) -> Unit)? = null

    fun setOnItemClickListener(listener: (HotelData) -> Unit) {
        this.listener = listener
    }

    inner class HighestHotelViewHolder(val binding: ItemHighestHotelBinding) :
        RecyclerView.ViewHolder(binding.root)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighestHotelViewHolder {
        return HighestHotelViewHolder(
            ItemHighestHotelBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return hotels.size.coerceAtMost(8)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HighestHotelViewHolder, position: Int) {
        val hotel = hotels[position]
        val formatter = DecimalFormat("#,###")
        holder.binding.tvNewPrice.text = "VND ${formatter.format(hotel.new_price)}"
        holder.binding.tvOldPrice.apply {
            text = "VND ${formatter.format(hotel.old_price)}"
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        val imageUrl = hotel.image.firstOrNull()
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(context)
                .load(imageUrl)
                .into(holder.binding.mainImg)
        }

        holder.binding.tvHotelName.text = hotel.hotel_name

        holder.binding.star.removeAllViews()
        repeat(hotel.hotel_star.toInt()) {
            val starView = AppCompatImageView(holder.binding.root.context).apply {
                setImageResource(R.drawable.ic_star) // icon sao
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            holder.binding.star.addView(starView)
        }

        holder.binding.root.setOnClickListener {
            listener?.invoke(hotel)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<HotelData>) {
        hotels = newList
        Log.e("sokhaachsan", hotels.size.toString())
        notifyDataSetChanged()
    }
}