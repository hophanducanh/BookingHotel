package com.btl.bookingHotel.model

data class ProfileResponse(
    val data: ProfileData,
    val message: String,
    val status: String
)

data class ProfileData(
    val access_token: String,
    val avatar_url: String,
    val country: String,
    val date_of_birth: String,
    val email: String,
    val id: Int,
    val phone_number: String,
    val point: Int,
    val user_name: String
)