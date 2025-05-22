package com.btl.bookingHotel.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.btl.bookingHotel.model.MyDiscountData
import com.btl.bookinghotel.databinding.ItemMyCouponBinding

class MyCouponAdapter(
    private val discounts: List<MyDiscountData>,
    private val onItemClick: (MyDiscountData) -> Unit
) : RecyclerView.Adapter<MyCouponAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemMyCouponBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(discount: MyDiscountData) {
            binding.tvCouponName.text = discount.discount_name
            binding.tvCouponAmount.text = "Số lượng: ${discount.amount}"

            binding.root.setOnClickListener {
                onItemClick(discount)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMyCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = discounts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(discounts[position])
    }
}
