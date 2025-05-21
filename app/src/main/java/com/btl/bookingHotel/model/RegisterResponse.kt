package com.btl.bookingHotel.model

data class RegisterResponse(
    val status: String,
    val message: String,
    val data: RegisterUserData
)

data class RegisterUserData(
    val id: Int,
    val email: String,
    val phone_number: String,
    val user_name: String,
    val country: String,
    val avatar_url: String,
    val access_token: String
)