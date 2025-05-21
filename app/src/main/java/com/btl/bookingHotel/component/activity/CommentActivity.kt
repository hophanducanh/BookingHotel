package com.btl.bookingHotel.component.activity

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.btl.bookingHotel.adapter.ReviewImageAdapter
import com.btl.bookingHotel.api.ApiClient
import com.btl.bookingHotel.model.CommentRequest
import com.btl.bookinghotel.R
import com.btl.bookinghotel.databinding.ActivityCommentBinding
import com.btl.bookinghotel.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CommentActivity : BaseActivity<ActivityCommentBinding>() {
    private var hotelId: Int? = null
    private var rating: Double? = null
    private lateinit var hotelName: String
    private lateinit var hotelAddress: String
    private lateinit var hotelFirstImage: String

    private val imageUris = mutableListOf<Uri>()

    override fun initData() {

    }

    override fun initView() {
        hotelId = intent.getIntExtra("hotel_id", -1)
        hotelName = intent.getStringExtra("hotel_name") ?: ""
        hotelAddress = intent.getStringExtra("hotel_address") ?: ""
        hotelFirstImage = intent.getStringExtra("hotel_first_image") ?: ""
        binding.tvLocation.text = hotelAddress

        Glide.with(this)
            .load(hotelFirstImage)
            .into(binding.mainImg)

        binding.tvHotelName.text = hotelName

        binding.listItem.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    }

    override fun initListener() {
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("DefaultLocale")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                rating = progress / 10.0
                binding.tvScore.text = String.format("%.1f", rating)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        binding.icUploadImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.btnSend.setOnClickListener {
            postCommentWithImages()
        }
    }

    override fun getViewBinding(): ActivityCommentBinding {
        return ActivityCommentBinding.inflate(layoutInflater)
    }

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        imageUris.clear()
        imageUris.addAll(uris.take(3))
        binding.listItem.adapter = ReviewImageAdapter(imageUris)
    }

    private fun uriToFile(uri: Uri, context: Context): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File.createTempFile("upload", ".jpg", context.cacheDir)
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }

    private fun postCommentWithImages() {
        val context = this
        val commentText = binding.etFeedback.text.toString()

        if (commentText.isBlank() || rating == null || hotelId == null) {
            Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.btnSend.isEnabled = false

        val ratingRequestBody = rating.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val hotelIdRequestBody = hotelId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val commentRequestBody = commentText.toRequestBody("text/plain".toMediaTypeOrNull())

        val imageParts = imageUris.mapIndexed { index, uri ->
            val file = uriToFile(uri, context)
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", file.name, requestFile) // tất cả dùng key "image"
        }

        val service = ApiClient.create(context)

        service.postComment(ratingRequestBody, hotelIdRequestBody, commentRequestBody, imageParts)
            .enqueue(object : Callback<CommentRequest> {
                override fun onResponse(call: Call<CommentRequest>, response: Response<CommentRequest>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Đăng bình luận thành công!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(context, "Lỗi phản hồi từ server", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CommentRequest>, t: Throwable) {
                    Toast.makeText(context, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

}