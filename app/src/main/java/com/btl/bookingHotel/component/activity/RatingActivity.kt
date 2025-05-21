package com.btl.bookingHotel.component.activity

import android.R
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.btl.bookingHotel.adapter.CommentAdapter
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
    private lateinit var commentAdapter: CommentAdapter
    private val sortOptions =
        listOf("Mới nhất", "Cũ nhất", "Đánh giá thấp nhất", "Đánh giá cao nhất")

    override fun initView() {
        commentAdapter = CommentAdapter(this, emptyList())
        binding.listItem.adapter = commentAdapter
        binding.listItem.layoutManager = LinearLayoutManager(this)
        binding.listItem.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        val spinnerAdapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            sortOptions
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOrder.adapter = spinnerAdapter
        binding.spinnerOrder.setSelection(0)
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

        binding.spinnerOrder.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val sortType = when (position) {
                    0 -> "newest"
                    1 -> "oldest"
                    2 -> "lowest"
                    3 -> "highest"
                    else -> "newest"
                }

                hotelId?.let { fetchComments(it, sortType) }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun fetchComments(hotelId: Int, sortType: String) {
        val apiService = ApiClient.create(this)
        apiService.getComments(hotelId, sortType).enqueue(object : Callback<CommentResponse> {
            override fun onResponse(
                call: Call<CommentResponse>,
                response: Response<CommentResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val commentList = response.body()!!.data.comments

                    if (commentList.isNotEmpty()) {
                        binding.tvAmountRating.text = "${commentList.size} đánh giá"
                        binding.tvNoComment.visibility = View.GONE
                        commentAdapter.setData(commentList)
                    } else {
                        binding.tvAmountRating.text = "Chưa có đánh giá"
                        binding.tvNoComment.visibility = View.VISIBLE
                        binding.listItem.visibility = View.GONE
                    }
                } else {
                    val errorCode = response.code()
                    val errorBody = response.errorBody()?.string()
                    android.util.Log.e(
                        "API_ERROR",
                        "Lỗi phản hồi từ server - Code: $errorCode\nBody: $errorBody"
                    )
                    showError("Lỗi phản hồi từ server.")
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                android.util.Log.e("API_ERROR", "Không thể kết nối đến server", t)
                showError("Không thể kết nối đến server.")
            }
        })
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        binding.tvNoComment.visibility = View.VISIBLE
        binding.tvAmountRating.text = "Chưa có đánh giá"
    }


    override fun getViewBinding(): ActivityRatingBinding {
        return ActivityRatingBinding.inflate(layoutInflater)
    }

}