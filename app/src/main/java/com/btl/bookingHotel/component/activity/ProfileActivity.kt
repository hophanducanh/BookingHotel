package com.btl.bookingHotel.component.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.btl.bookingHotel.api.ApiClient
import com.btl.bookingHotel.model.ProfileResponse
import com.btl.bookingHotel.utils.Constants
import com.btl.bookinghotel.databinding.ActivityProfileBinding
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProfileActivity : BaseActivity<ActivityProfileBinding>() {

    private var imageUri: Uri? = null
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                imageUri = it.data?.data
                binding.imgAvatar.setImageURI(imageUri)
            }
        }

    override fun initView() {
        val avatarUrl = intent.getStringExtra("avatar_url")
        val email = intent.getStringExtra("email")
        val phoneNumber = intent.getStringExtra("phone_number")
        val userName = intent.getStringExtra("user_name")

        Glide.with(this)
            .load(Constants.BASE_URL + avatarUrl)
            .into(binding.imgAvatar)

        binding.etUserName.setText(userName ?: "")
        binding.etEmail.setText(email ?: "")
        binding.etPhoneNumber.setText(phoneNumber ?: "")

        // Load ngày sinh từ SharedPreferences
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val savedBirthday = sharedPref.getString("birthday", null)

        if (!savedBirthday.isNullOrEmpty()) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(savedBirthday)
            val displayBirthday = if (date != null) outputFormat.format(date) else savedBirthday

            binding.tvBirthday.text = displayBirthday
        }
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.imgAvatar.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    imagePickerLauncher.launch(intent)
                }
        }

        binding.btnCalendar.setOnClickListener {
            showDatePicker()
        }

        binding.btnApply.setOnClickListener {
            updateProfile()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                binding.tvBirthday.text = selectedDate
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun updateProfile() {
        val userName = binding.etUserName.text.toString()
        val email = binding.etEmail.text.toString()
        val phone = binding.etPhoneNumber.text.toString()
        val birthday = binding.tvBirthday.text.toString()
        val country = "Vietnam"

        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val date = inputFormat.parse(birthday)
        val formattedDate = if (date != null) outputFormat.format(date) else ""

        val requestDob = formattedDate.toRequestBody("text/plain".toMediaTypeOrNull())
        val requestUserName = userName.toRequestBody("text/plain".toMediaTypeOrNull())
        val requestEmail = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val requestPhone = phone.toRequestBody("text/plain".toMediaTypeOrNull())
        val requestCountry = country.toRequestBody("text/plain".toMediaTypeOrNull())

        val avatarPart = imageUri?.let {
            val inputStream = contentResolver.openInputStream(it)
            val bytes = inputStream?.readBytes()
            val requestFile = bytes?.toRequestBody("image/*".toMediaTypeOrNull())
            requestFile?.let { rf ->
                MultipartBody.Part.createFormData("avatar", "avatar.jpg", rf)
            }
        }

        val apiService = ApiClient.create(this)
        val call = apiService.updateUserProfile(
            userName = requestUserName,
            email = requestEmail,
            phoneNumber = requestPhone,
            dateOfBirth = requestDob,
            country = requestCountry,
            avatar = avatarPart
        )

        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful) {
                    val profileData = response.body()?.data
                    profileData?.access_token?.let { token ->
                        saveTokenToSharedPreferences(token)
                    }

                    // Lưu ngày sinh vào SharedPreferences
                    saveBirthdayToSharedPreferences(formattedDate)

                    showToast("Cập nhật thành công")
                } else {
                    showToast("Cập nhật thất bại: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                showToast("Lỗi kết nối: ${t.message}")
            }
        })
    }

    private fun saveTokenToSharedPreferences(token: String) {
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        sharedPref.edit().putString("access_token", token).apply()
    }

    private fun saveBirthdayToSharedPreferences(birthday: String) {
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        sharedPref.edit().putString("birthday", birthday).apply()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun getViewBinding(): ActivityProfileBinding {
        return ActivityProfileBinding.inflate(layoutInflater)
    }
}
