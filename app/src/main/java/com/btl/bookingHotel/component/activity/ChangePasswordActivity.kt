package com.btl.bookingHotel.component.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.btl.bookingHotel.api.ApiClient
import com.btl.bookingHotel.model.ChangePasswordRequest
import com.btl.bookingHotel.model.ChangePasswordResponse
import com.btl.bookinghotel.R
import com.btl.bookinghotel.databinding.ActivityChangePasswordBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding>() {
    override fun initView() {

    }

    override fun initData() {
    }

    override fun initListener() {
        binding.btnUpdate.setOnClickListener {
            val oldPass = binding.etOldPass.text.toString()
            val newPass = binding.etNewPass.text.toString()
            val rePass = binding.etReEnterPass.text.toString()

            if (newPass != rePass) {
                Toast.makeText(this, "Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            changePassword(oldPass, newPass)
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun changePassword(current: String, new: String) {

        ApiClient.create(this).changePassword(ChangePasswordRequest(current, new)).enqueue(object :
            Callback<ChangePasswordResponse> {
            override fun onResponse(
                call: Call<ChangePasswordResponse>,
                response: Response<ChangePasswordResponse>
            ) {
                if (response.isSuccessful) {
                    val res = response.body()
                    Toast.makeText(
                        this@ChangePasswordActivity,
                         "Đổi mật khẩu thành công",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@ChangePasswordActivity,
                        "Sai mật khẩu cũ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                Toast.makeText(
                    this@ChangePasswordActivity,
                    "Lỗi kết nối: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun getViewBinding(): ActivityChangePasswordBinding {
        return ActivityChangePasswordBinding.inflate(layoutInflater)
    }

}