package com.btl.bookingHotel.component.activity

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.btl.bookingHotel.model.BookingData
import com.btl.bookinghotel.R
import com.btl.bookinghotel.databinding.ActivityCompleteBookingBinding
import com.btl.bookinghotel.databinding.ActivitySelectedRoomBinding
import java.text.DecimalFormat

class CompleteBookingActivity : BaseActivity<ActivityCompleteBookingBinding>() {

    override fun initView() {
        val bookingData = intent.getSerializableExtra("booking_data") as? BookingData
        val selectedRoomNumber = intent.getStringExtra("selected_room_number") ?: "N/A"
        if (bookingData != null) {
            binding.tvHotelName.text = bookingData.hotel_info.hotel_name
            binding.tvHotelAddress.text = bookingData.hotel_info.address
            binding.star.removeAllViews()
            repeat(bookingData.hotel_info.star.toInt()) {
                val starView = AppCompatImageView(binding.root.context).apply {
                    setImageResource(R.drawable.ic_star)
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
                binding.star.addView(starView)
            }

            binding.tvCheckin.text = bookingData.booking_info.check_in
            binding.tvCheckout.text = bookingData.booking_info.check_out

            binding.tvRoomType.text = bookingData.room_info.room_type
            binding.tvRoomNumber.text = "Phòng $selectedRoomNumber"

            binding.tvPeople.text = "${bookingData.booking_info.number_of_people} người lớn"
            binding.tvKid.text = "${bookingData.booking_info.number_of_children} trẻ em"

            val newPrice = bookingData.booking_info.total_price
            val oldPrice = bookingData.booking_info.base_price.toDouble()

            val formatter = DecimalFormat("#,###")
            binding.tvNewPrice.text = "${formatter.format(newPrice)} VND"

            if (newPrice == oldPrice) {
                binding.tvOldPrice.visibility = ViewGroup.GONE
            } else {
                binding.tvOldPrice.apply {
                    visibility = ViewGroup.VISIBLE
                    text = "${formatter.format(oldPrice)} VND"
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            }
        }
    }

    override fun initData() {
    }

    override fun initListener() {

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun getViewBinding(): ActivityCompleteBookingBinding {
        return ActivityCompleteBookingBinding.inflate(layoutInflater)
    }
}