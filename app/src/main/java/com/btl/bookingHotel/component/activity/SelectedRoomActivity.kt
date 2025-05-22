package com.btl.bookingHotel.component.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.btl.bookingHotel.adapter.AvailableRoomAdapter
import com.btl.bookingHotel.api.ApiClient
import com.btl.bookingHotel.dialog.ConfirmBookingDialog
import com.btl.bookingHotel.dialog.CouponBottomSheetLayout
import com.btl.bookingHotel.model.BookingRequest
import com.btl.bookingHotel.model.BookingResponse
import com.btl.bookingHotel.model.RoomData
import com.btl.bookingHotel.model.RoomResponse
import com.btl.bookinghotel.databinding.ActivitySelectedRoomBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SelectedRoomActivity : BaseActivity<ActivitySelectedRoomBinding>() {
    private lateinit var adapter: AvailableRoomAdapter
    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    private var hotelId: Int? = null

    private var selectedDiscountId: Int? = null

    override fun initView() {
        adapter = AvailableRoomAdapter(this, mutableListOf())
        binding.listItem.adapter = adapter
        binding.listItem.layoutManager = LinearLayoutManager(this)
    }

    override fun initData() {
        hotelId = intent.getIntExtra("hotel_id", -1)
    }

    @SuppressLint("SetTextI18n")
    override fun initListener() {
        binding.btnCalendar.setOnClickListener {
            showStartDatePicker()
        }

        binding.btnAddPeople.setOnClickListener{
            val current = binding.tvPeople.text.toString().toIntOrNull() ?: 1
            val newValue = if (current < 5) current + 1 else 5
            binding.tvPeople.text = newValue.toString()
        }

        binding.btnMinusPeople.setOnClickListener{
            val current = binding.tvPeople.text.toString().toIntOrNull() ?: 1
            val newValue = if (current > 1) current - 1 else 1
            binding.tvPeople.text = newValue.toString()
        }

        binding.btnAddKid.setOnClickListener{
            val current = binding.tvKid.text.toString().toIntOrNull() ?: 1
            val newValue = if (current < 3) current + 1 else 3
            binding.tvKid.text = newValue.toString()
        }

        binding.btnMinusKid.setOnClickListener{
            val current = binding.tvKid.text.toString().toIntOrNull() ?: 1
            val newValue = if (current > 0) current - 1 else 0
            binding.tvKid.text = newValue.toString()
        }

        binding.btnCoupon.setOnClickListener {
            val couponSheet = CouponBottomSheetLayout()
            couponSheet.listener = object : CouponBottomSheetLayout.OnCouponSelectedListener {
                override fun onCouponSelected(couponName: String, discountId: Int) {
                    binding.tvCoupon.text = couponName
                    selectedDiscountId = discountId
                }
            }
            couponSheet.show(supportFragmentManager, "CouponBottomSheet")
        }

        binding.btnApply.setOnClickListener{
            fetchAvailableRooms()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnDone.setOnClickListener {
            val dialog = ConfirmBookingDialog(
                context = this,
                onConfirm = { performBooking() },
                onCancel = {  }
            )
            dialog.show()
        }
    }

    private fun showStartDatePicker() {
        val today = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                startDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                showEndDatePicker()
            },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = today.timeInMillis
            setTitle("Chọn ngày bắt đầu")
        }.show()
    }

    private fun showEndDatePicker() {
        val minEndDate = Calendar.getInstance().apply {
            timeInMillis = startDate!!.timeInMillis
            add(Calendar.DAY_OF_MONTH, 1)
        }

        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                endDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                displaySelectedDates()
            },
            minEndDate.get(Calendar.YEAR),
            minEndDate.get(Calendar.MONTH),
            minEndDate.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = minEndDate.timeInMillis
            setTitle("Chọn ngày kết thúc")
        }.show()
    }

    @SuppressLint("SetTextI18n")
    private fun displaySelectedDates() {
        if (startDate != null && endDate != null) {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val start = sdf.format(startDate!!.time)
            val end = sdf.format(endDate!!.time)
            binding.tvCheckinCheckout.text = "$start - $end"
        }
    }

    override fun getViewBinding(): ActivitySelectedRoomBinding {
        return ActivitySelectedRoomBinding.inflate(layoutInflater)
    }

    private fun performBooking() {
        val selectedRoom = adapter.getSelectedRoomNumber()
        val selectedRoomNumberString = adapter.getSelectedRoomNumberString()
        if (selectedRoom == null) {
            Toast.makeText(this, "Vui lòng chọn phòng", Toast.LENGTH_SHORT).show()
            return
        }

        if (startDate == null || endDate == null) {
            Toast.makeText(this, "Vui lòng chọn ngày nhận và trả phòng", Toast.LENGTH_SHORT).show()
            return
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val checkIn = sdf.format(startDate!!.time)
        val checkOut = sdf.format(endDate!!.time)
        val adults = binding.tvPeople.text.toString().toIntOrNull() ?: 1
        val kids = binding.tvKid.text.toString().toIntOrNull() ?: 0

        val bookingRequest = BookingRequest(
            room_id = selectedRoom,
            number_of_people = adults,
            number_of_children = kids,
            check_in = checkIn,
            check_out = checkOut,
            user_discount_id = selectedDiscountId
        )

        ApiClient.create(this).createBooking(bookingRequest)
            .enqueue(object : Callback<BookingResponse> {
                override fun onResponse(call: Call<BookingResponse>, response: Response<BookingResponse>) {
                    if (response.isSuccessful) {
                        val bookingData = response.body()!!.data

                        val intent = Intent(this@SelectedRoomActivity, CompleteBookingActivity::class.java)
                        intent.putExtra("booking_data", bookingData)
                        intent.putExtra("selected_room_number", selectedRoomNumberString)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@SelectedRoomActivity, "Đặt phòng thất bại", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<BookingResponse>, t: Throwable) {
                    Toast.makeText(this@SelectedRoomActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun fetchAvailableRooms() {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        if (startDate == null || endDate == null) {
            Toast.makeText(this, "Vui lòng chọn ngày nhận và trả phòng", Toast.LENGTH_SHORT).show()
            return
        }

        val checkIn = sdf.format(startDate!!.time)
        val checkOut = sdf.format(endDate!!.time)

        val adults = binding.tvPeople.text.toString().toIntOrNull() ?: 1
        val kids = binding.tvKid.text.toString().toIntOrNull() ?: 0
        val roomTypeRaw = determineRoomType(adults, kids)

        val roomTypes = roomTypeRaw.split(",").map { it.trim() }
        val allRooms = mutableListOf<RoomData>()

        var responsesReceived = 0
        var firstResponse = true

        for (type in roomTypes) {
            ApiClient.create(this).getAvailableRooms(
                hotelId = hotelId,
                roomType = type,
                checkIn = checkIn,
                checkOut = checkOut
            ).enqueue(object : Callback<RoomResponse> {
                override fun onResponse(call: Call<RoomResponse>, response: Response<RoomResponse>) {
                    if (firstResponse) {
                        allRooms.clear()
                        firstResponse = false
                    }
                    responsesReceived++

                    if (response.isSuccessful) {
                        response.body()?.data?.let { allRooms.addAll(it) }
                    }

                    if (responsesReceived == roomTypes.size) {
                        adapter.setData(allRooms)
                        updateViewBasedOnResult(allRooms.isNotEmpty())
                    }
                }

                override fun onFailure(call: Call<RoomResponse>, t: Throwable) {
                    if (firstResponse) {
                        allRooms.clear()
                        firstResponse = false
                    }
                    responsesReceived++

                    if (responsesReceived == roomTypes.size) {
                        adapter.setData(allRooms)
                        Toast.makeText(
                            this@SelectedRoomActivity,
                            "Tìm thấy ${allRooms.size} phòng (một số lỗi mạng)",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        }
    }

    private fun determineRoomType(adults: Int, kids: Int): String {
        val totalPeople = adults + kids
        return when {
            totalPeople in 1..2 -> "Phòng đơn"
            totalPeople in 3..5 -> "Phòng đôi,Phòng ba giường"
            totalPeople in 6..8 -> "Phòng ba giường,Phòng hai giường lớn"
            else -> "Không có phòng"
        }
    }

    private fun updateViewBasedOnResult(hasRoom: Boolean) {
        binding.textInput.visibility = View.GONE

        if (hasRoom) {
            binding.listItem.visibility = View.VISIBLE
            binding.textNotAvailble.visibility = View.GONE
        } else {
            binding.listItem.visibility = View.GONE
            binding.textNotAvailble.visibility = View.VISIBLE
        }
    }
}
