package com.btl.bookingHotel.component.activity

import android.content.Intent
import com.btl.bookingHotel.api.ApiClient
import com.btl.bookingHotel.model.CommentResponse
import com.btl.bookinghotel.databinding.ActivityRatingBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RatingActivity : BaseActivity<ActivityRatingBinding>() {
    private var hotelId: Int? = null
    private lateinit var hotelName: String
    private lateinit var hotelAddress: String
    private lateinit var hotelFirstImage: String
    override fun initView() {
    }

    override fun initData() {
        hotelId = intent.getIntExtra("hotel_id", -1)
        hotelName = intent.getStringExtra("hotel_name") ?: ""
        hotelAddress = intent.getStringExtra("hotel_address") ?: ""
        hotelFirstImage = intent.getStringExtra("hotel_first_image") ?: ""

    }

    override fun initListener() {
        binding.btnRating.setOnClickListener {
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtra("hotel_id", hotelId)
            intent.putExtra("hotel_address", hotelAddress)
            intent.putExtra("hotel_first_image", hotelFirstImage)
            intent.putExtra("hotel_name", hotelName)
            startActivity(intent)
        }
    }

    private fun fetchComments(hotelId: Int) {
        val apiService = ApiClient.create(this)
        apiService.getComments(hotelId).enqueue(object : Callback<CommentResponse> {
            override fun onResponse(
                call: Call<CommentResponse>,
                response: Response<CommentResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val commentResponse = response.body()!!
                    val comments = commentResponse.data.comments

                    if (comments.isEmpty()) {
                    } else {
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
            }
        })
    }


    override fun getViewBinding(): ActivityRatingBinding {
        return ActivityRatingBinding.inflate(layoutInflater)
    }

}