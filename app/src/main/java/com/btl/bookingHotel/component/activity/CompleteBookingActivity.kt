package com.btl.bookingHotel.component.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.btl.bookinghotel.R
import com.btl.bookinghotel.databinding.ActivityCompleteBookingBinding
import com.btl.bookinghotel.databinding.ActivitySelectedRoomBinding

class CompleteBookingActivity : BaseActivity<ActivityCompleteBookingBinding>() {

    override fun initView() {
        val selectedRoom = intent.getStringExtra("selected_room_number")
        val checkIn = intent.getStringExtra("check_in")
        val checkOut = intent.getStringExtra("check_out")
        val adults = intent.getIntExtra("adults", 1)
        val kids = intent.getIntExtra("kids", 0)
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun getViewBinding(): ActivityCompleteBookingBinding {
        return ActivityCompleteBookingBinding.inflate(layoutInflater)
    }
}