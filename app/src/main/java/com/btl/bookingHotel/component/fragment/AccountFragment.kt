package com.btl.bookingHotel.component.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.btl.bookingHotel.component.activity.PointActivity
import com.btl.bookingHotel.api.ApiClient
import com.btl.bookingHotel.dialog.CouponBottomSheetLayout
import com.btl.bookingHotel.model.UserData
import com.btl.bookingHotel.model.UserResponse
import com.btl.bookinghotel.databinding.FragmentAccountBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AccountFragment : BaseFragment<FragmentAccountBinding>() {
    private var fetchedUser: UserData? = null
    override fun initView(view: View) {
        fetchProfile()
    }

    override fun initListener() {
        binding.btnCoupon.setOnClickListener {
            val bottomSheet = CouponBottomSheetLayout()
            bottomSheet.show(childFragmentManager, "CouponBottomSheet")
        }

        binding.btnPoint.setOnClickListener {
            val point = fetchedUser?.point ?: 0
            val intent = Intent(requireContext(), PointActivity::class.java)
            intent.putExtra("point", point)
            startActivity(intent)
        }
    }

    private fun fetchProfile() {
        ApiClient.create(requireContext()).getProfile()
            .enqueue(object : Callback<UserResponse> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful && response.body()?.status == "success") {
                        val data = response.body()!!.data
                        fetchedUser = data
                        Glide.with(requireContext())
                            .load("https://special-space-disco-975v7wwppxv9h7457-5000.app.github.dev/" + data.avatar_url)
                            .into(binding.imgAvatar)
                        binding.tvUserName.text = "Xin chào, ${data?.user_name}"
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Lỗi: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAccountBinding {
        return FragmentAccountBinding.inflate(inflater)
    }

}