package com.btl.bookingHotel.component.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.btl.bookinghotel.R
import com.btl.bookinghotel.databinding.ActivityPointBinding

class PointActivity : BaseActivity<ActivityPointBinding>(){
    @SuppressLint("SetTextI18n")
    override fun initView() {
        val point = intent.getIntExtra("point", 0)
        binding.tvPoint.text = "$point Đ"
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun getViewBinding(): ActivityPointBinding {
        return ActivityPointBinding.inflate(layoutInflater)
    }

}