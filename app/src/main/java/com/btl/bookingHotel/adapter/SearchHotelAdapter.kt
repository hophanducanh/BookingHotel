package com.btl.bookingHotel.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.btl.bookingHotel.model.HotelData
import com.btl.bookinghotel.R
import com.btl.bookinghotel.databinding.ItemResultBinding
import java.text.DecimalFormat

class SearchHotelAdapter(private val context: Context, private var hotels: List<HotelData>) :
    RecyclerView.Adapter<SearchHotelAdapter.SearchHotelViewHolder>() {

    private var listener: ((HotelData) -> Unit)? = null

    fun setOnItemClickListener(listener: (HotelData) -> Unit) {
        this.listener = listener
    }

    inner class SearchHotelViewHolder(val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHotelViewHolder {
        return SearchHotelViewHolder(
            ItemResultBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = hotels.size

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onBindViewHolder(holder: SearchHotelViewHolder, position: Int) {
        val hotel = hotels[position]
        with(holder.binding) {
            tvHotelName.text = hotel.hotel_name
            val formatter = DecimalFormat("#,###")
            tvNewPrice.text = "VND ${formatter.format(hotel.new_price)}"
            tvOldPrice.apply {
                text = "VND ${formatter.format(hotel.old_price)}"
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            tvDistance.text = hotel.distance
            star.removeAllViews()
            repeat(hotel.hotel_star.toInt()) {
                val starView = AppCompatImageView(root.context).apply {
                    setImageResource(R.drawable.ic_star)
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
                star.addView(starView)
            }

            if (hotel.user_rating == null) {
                tvNoRate.visibility = View.VISIBLE
                frameRating.visibility = View.GONE
                tvRating.visibility = View.GONE
            } else {
                val rating = hotel.user_rating ?: 0f
                tvNoRate.visibility = View.GONE
                frameRating.visibility = View.VISIBLE
                tvRating.visibility = View.VISIBLE

                tvRatingScore.text = String.format("%.1f", rating)

                tvRating.text = when (rating) {
                    in 0f..5f -> "Khá tệ"
                    in 5f..6f -> "Tàm tạm"
                    in 6f..7f -> "Khá ổn"
                    in 7f..8f -> "Tốt"
                    in 8f..9f -> "Rất tốt"
                    in 9f..10f -> "Tuyệt vời"
                    else -> "Không xác định"
                }
            }

            val imageAdapter = ViewPagerImageAdapter(context, hotel.image)
            imageViewPager.adapter = imageAdapter
            imageViewPager.setCurrentItem(0, false)
            btnBack.alpha = 0.3f
            btnBack.isEnabled = false
            btnNext.alpha = 1f
            btnNext.isEnabled = true

            imageViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    btnBack.apply {
                        isEnabled = position != 0
                        alpha = if (position == 0) 0.3f else 1f
                    }

                    btnNext.apply {
                        isEnabled = position != imageAdapter.itemCount - 1
                        alpha = if (position == imageAdapter.itemCount - 1) 0.3f else 1f
                    }
                }
            })

            btnBack.setOnClickListener {
                val current = imageViewPager.currentItem
                if (current > 0) imageViewPager.setCurrentItem(current - 1, true)
            }

            btnNext.setOnClickListener {
                val current = imageViewPager.currentItem
                if (current < imageAdapter.itemCount - 1) imageViewPager.setCurrentItem(
                    current + 1,
                    true
                )
            }

            root.setOnClickListener {
                listener?.invoke(hotel)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<HotelData>) {
        hotels = newList
        notifyDataSetChanged()
    }
}
