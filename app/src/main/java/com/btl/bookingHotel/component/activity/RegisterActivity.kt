package com.btl.bookingHotel.component.activity

//noinspection SuspiciousImport
import android.R
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.btl.bookingHotel.api.ApiClient
import com.btl.bookingHotel.dialog.RegisterSuccessDialog
import com.btl.bookingHotel.model.RegisterResponse
import com.btl.bookinghotel.databinding.ActivityRegisterBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    private var imageUri: Uri? = null
    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                imageUri = it.data?.data
                binding.imgAvatar.setImageURI(imageUri)
            }
        }

    override fun initView() {
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
        binding.btnRegister.setOnClickListener {
            if (imageUri == null) {
                Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            uploadRegisterData()
        }
    }

    private fun uploadRegisterData() {
        binding.progressBar.visibility = View.VISIBLE

        val file = getFileFromUri(imageUri!!)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val avatar = MultipartBody.Part.createFormData("avatar", file.name, requestFile)

        val email = createPartFromString(binding.tvEmail.text.toString())
        val password = createPartFromString(binding.tvPassword.text.toString())
        val phone = createPartFromString(binding.tvUserNumber.text.toString())
        val username = createPartFromString(binding.tvUserName.text.toString())
        val country = createPartFromString(binding.tvCountry.text.toString())

        ApiClient.create(this).registerUser(email, password, phone, username, country, avatar)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body() != null) {
                        showSuccessDialog()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Lỗi: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Thất bại: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun createPartFromString(data: String): RequestBody {
        return data.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun getFileFromUri(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)!!
        val file = File(cacheDir, "avatar_${System.currentTimeMillis()}.jpg")
        file.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        return file
    }

    private fun showSuccessDialog() {
        val dialog = RegisterSuccessDialog(this, R.style.Theme_Material_Light_Dialog)
        dialog.show()
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 3000)
    }

    override fun getViewBinding(): ActivityRegisterBinding {
        return ActivityRegisterBinding.inflate(layoutInflater)
    }
}