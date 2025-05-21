package com.btl.bookingHotel.component.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.btl.bookingHotel.adapter.DetailsImageAdapter
import com.btl.bookingHotel.adapter.ViewPagerDetailAdapter
import com.btl.bookingHotel.model.HotelData
import com.btl.bookinghotel.R
import com.btl.bookinghotel.databinding.ActivityDetailBinding
import org.json.JSONArray
import java.text.DecimalFormat

class DetailActivity : BaseActivity<ActivityDetailBinding>() {
    private lateinit var hotel: HotelData
    override fun initView() {
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        hotel = intent.getSerializableExtra("hotelData") as HotelData
        binding.tvHotelName.text = hotel.hotel_name

        if (hotel.user_rating == null) {
            binding.tvNoRate.visibility = View.VISIBLE
            binding.rating.visibility = View.GONE
        } else {
            val rating = hotel.user_rating ?: 0f
            binding.tvNoRate.visibility = View.GONE
            binding.rating.visibility = View.VISIBLE

            binding.tvRating.text = String.format("%.1f", rating)
        }

        val formatter = DecimalFormat("#,###")
        binding.tvNewPrice.text = "VND ${formatter.format(hotel.new_price)}"
        binding.tvOldPrice.apply {
            text = "VND ${formatter.format(hotel.old_price)}"
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        binding.star.removeAllViews()
        repeat(hotel.hotel_star.toInt()) {
            val starView = AppCompatImageView(binding.root.context).apply {
                setImageResource(R.drawable.ic_star)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            binding.star.addView(starView)
        }

        binding.tvAddress.text = hotel.address
        binding.tvDescription.text = hotel.descriptions

        val policyList = JSONArray(hotel.policies).let { jsonArray ->
            List(jsonArray.length()) { i -> jsonArray.getString(i) }
        }
        val formattedPolicies = policyList.joinToString(separator = "\n") { "• $it" }
        binding.tvPolicies.text = formattedPolicies

        val formattedFacilities = hotel.facilities.joinToString("\n") {
            it.replaceFirstChar { char -> char.uppercaseChar() }
        }
        binding.tvFacilities.text = formattedFacilities

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        val adapter = DetailsImageAdapter(this, hotel.image) { position ->
            showFullScreenImage(hotel.image, position)
        }
        binding.recyclerView.adapter = adapter

    }

    private fun showFullScreenImage(images: List<String>, position: Int) {
        binding.fullscreenContainer.visibility = View.VISIBLE
        val fullScreenAdapter = ViewPagerDetailAdapter(this, images)
        binding.fullscreenViewPager.adapter = fullScreenAdapter
        binding.fullscreenViewPager.setCurrentItem(position, false)
    }

    @SuppressLint("SetTextI18n")
    override fun initListener() {
        var isDescriptionExpanded = false
        binding.btnMoreDescription.setOnClickListener {
            if (isDescriptionExpanded) {
                binding.tvDescription.maxLines = 4
                binding.btnMoreDescription.text = "Xem thêm"
            } else {
                binding.tvDescription.maxLines = Integer.MAX_VALUE
                binding.btnMoreDescription.text = "Rút gọn"
            }
            isDescriptionExpanded = !isDescriptionExpanded
        }

        var isPoliciesExpanded = false
        binding.btnMorePolicies.setOnClickListener {
            if (isPoliciesExpanded) {
                binding.tvPolicies.maxLines = 4
                binding.btnMorePolicies.text = "Xem thêm"
            } else {
                binding.tvPolicies.maxLines = Integer.MAX_VALUE
                binding.btnMorePolicies.text = "Rút gọn"
            }
            isPoliciesExpanded = !isPoliciesExpanded
        }

        var isFacilitiesExpanded = false
        binding.btnMoreFacilities.setOnClickListener {
            if (isFacilitiesExpanded) {
                binding.tvFacilities.maxLines = 4
                binding.btnMoreFacilities.text = "Xem thêm"
            } else {
                binding.tvFacilities.maxLines = Integer.MAX_VALUE
                binding.btnMoreFacilities.text = "Rút gọn"
            }
            isFacilitiesExpanded = !isFacilitiesExpanded
        }

        binding.btnCloseFullscreen.setOnClickListener {
            binding.fullscreenContainer.visibility = View.GONE
        }

        binding.btnComment.paintFlags = binding.btnComment.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.btnComment.setOnClickListener {
            val intent = Intent(this, RatingActivity::class.java)
            intent.putExtra("hotel_id", hotel.id)
            intent.putExtra("hotel_address", hotel.address)
            intent.putExtra("hotel_name", hotel.hotel_name)
            val firstImage = if (hotel.image.isNotEmpty()) hotel.image[0] else ""
            intent.putExtra("hotel_first_image", firstImage)
            startActivity(intent)
        }

        binding.btnApply.setOnClickListener {
            val intent = Intent(this, SelectedRoomActivity::class.java)
            intent.putExtra("hotel_id", hotel.id)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun getViewBinding(): ActivityDetailBinding {
        return ActivityDetailBinding.inflate(layoutInflater)
    }

}