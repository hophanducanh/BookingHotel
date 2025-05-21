package com.btl.bookingHotel.model

data class MyDiscountResponse(
    val data: List<MyDiscountData>,
    val message: String,
    val status: String
)

data class MyDiscountData(
    val amount: Int,
    val discount_id: Int,
    val discount_name: String,
    val discount_value: Double,
    val is_used: Boolean,
    val point_required: Int
)