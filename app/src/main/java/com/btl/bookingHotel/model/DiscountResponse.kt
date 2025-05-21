package com.btl.bookingHotel.model

data class DiscountResponse(
    val data: List<DiscountData>,
    val message: String,
    val status: String
)
data class DiscountData(
    val description: String,
    val discount_name: String,
    val discount_value: Double,
    val id: Int,
    val point_required: Int
)