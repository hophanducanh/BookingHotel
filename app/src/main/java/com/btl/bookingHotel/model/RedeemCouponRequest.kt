package com.btl.bookingHotel.model

data class RedeemCouponRequest(val discount_id: Int)

data class CouponResponse(
    val data: CouponData,
    val message: String,
    val status: String
)

data class CouponData(
    val amount: Int,
    val discount_id: Int,
    val discount_value: Double,
    val remaining_points: Int
)