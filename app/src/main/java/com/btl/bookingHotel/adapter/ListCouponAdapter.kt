package com.btl.bookingHotel.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.btl.bookingHotel.model.DiscountData
import com.btl.bookinghotel.databinding.ItemCouponBinding

class ListCouponAdapter(
    private val discounts: List<DiscountData>,
    private val onItemClick: (DiscountData) -> Unit
) : RecyclerView.Adapter<ListCouponAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCouponBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(discount: DiscountData) {
            binding.tvCouponDescription.text = discount.description

            binding.root.setOnClickListener {
                onItemClick(discount)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = discounts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(discounts[position])
    }
}