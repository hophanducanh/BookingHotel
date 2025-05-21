package com.btl.bookingHotel.model

data class UserResponse(
    val data: UserData,
    val message: String,
    val status: String
)

data class UserData(
    val avatar_url: String,
    val country: String,
    val email: String,
    val id: Int,
    val phone_number: String,
    val point: Int,
    val user_name: String
)