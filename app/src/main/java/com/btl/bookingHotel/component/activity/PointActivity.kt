package com.btl.bookingHotel.component.activity

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.btl.bookingHotel.adapter.ListCouponAdapter
import com.btl.bookingHotel.api.ApiClient
import com.btl.bookingHotel.model.CouponResponse
import com.btl.bookingHotel.model.DiscountResponse
import com.btl.bookingHotel.model.MyDiscountResponse
import com.btl.bookingHotel.model.RedeemCouponRequest
import com.btl.bookinghotel.databinding.ActivityPointBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PointActivity : BaseActivity<ActivityPointBinding>() {
    private lateinit var adapter: ListCouponAdapter

    @SuppressLint("SetTextI18n")
    override fun initView() {
        val point = intent.getIntExtra("point", 0)
        binding.tvPoint.text = "$point Điểm"
    }

    override fun initData() {
        val apiService = ApiClient.create(this)

        binding.listItem.layoutManager = LinearLayoutManager(this)
        binding.listItem.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )

        apiService.getAllDiscount()
            .enqueue(object : Callback<DiscountResponse> {
                override fun onResponse(
                    call: Call<DiscountResponse>,
                    response: Response<DiscountResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val discountList = response.body()!!.data
                        adapter = ListCouponAdapter(discountList) { discount ->
                            apiService.changeDiscount(RedeemCouponRequest(discount.id))
                                .enqueue(object : Callback<CouponResponse> {
                                    @SuppressLint("SetTextI18n")
                                    override fun onResponse(
                                        call: Call<CouponResponse>,
                                        response: Response<CouponResponse>
                                    ) {
                                        if (response.isSuccessful && response.body() != null) {
                                            val res = response.body()!!

                                            when (res.message) {
                                                "Discount claimed successfully" -> {
                                                    Toast.makeText(
                                                        this@PointActivity,
                                                        "Đổi mã giảm giá thành công",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                    val updatedPoint = res.data.remaining_points
                                                    binding.tvPoint.text = "$updatedPoint Điểm"
                                                }

                                                else -> {
                                                    Toast.makeText(
                                                        this@PointActivity,
                                                        "Lỗi: ${res.message}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        } else {
                                            Toast.makeText(
                                                this@PointActivity,
                                                "Bạn chưa đủ điểm để đổi mã",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<CouponResponse>,
                                        t: Throwable
                                    ) {
                                        Toast.makeText(
                                            this@PointActivity,
                                            "Lỗi mạng: ${t.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                        }

                        binding.listItem.adapter = adapter
                    } else {
                        Toast.makeText(
                            this@PointActivity,
                            "Không thể tải danh sách mã giảm giá",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<DiscountResponse>, t: Throwable) {
                    Toast.makeText(
                        this@PointActivity,
                        "Lỗi kết nối: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }


    override fun initListener() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun getViewBinding(): ActivityPointBinding {
        return ActivityPointBinding.inflate(layoutInflater)
    }

}