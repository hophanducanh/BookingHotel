package com.btl.bookingHotel.component.activity

import android.content.Intent
import android.widget.Toast
import com.btl.bookingHotel.api.ApiClient
import com.btl.bookingHotel.model.LoginRequest
import com.btl.bookingHotel.model.LoginResponse
import com.btl.bookinghotel.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override fun initView() {

    }

    override fun initData() {
    }

    override fun initListener() {
        binding.btnLogin.setOnClickListener {
            val email = binding.tvEmail.text.toString().trim()
            val password = binding.tvPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            login(email, password)
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        ApiClient.create(this).loginUser(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {

                        val token = response.body()!!.data.access_token

                        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        sharedPref.edit().putString("access_token", token).apply()

                        val sharedPreferences = getSharedPreferences("Prefs", MODE_PRIVATE)
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                        Toast.makeText(
                            this@LoginActivity,
                            "Đăng nhập thành công!",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Email hoặc mật khẩu không đúng",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Lỗi: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    override fun getViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }

}