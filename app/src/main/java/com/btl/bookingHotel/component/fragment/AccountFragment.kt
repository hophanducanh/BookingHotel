package com.btl.bookingHotel.component.fragment

import android.R
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.btl.bookingHotel.api.ApiClient
import com.btl.bookingHotel.model.LocationResponse
import com.btl.bookingHotel.model.UserResponse
import com.btl.bookinghotel.databinding.FragmentAccountBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log


class AccountFragment : BaseFragment<FragmentAccountBinding>() {
    override fun initView(view: View) {
        fetchProfile()
    }

    override fun initListener() {
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
                        Glide.with(requireContext())
                            .load("https://scaling-space-telegram-pqq5wq6pqg6c964q-5000.app.github.dev/" + data.avatar_url)
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