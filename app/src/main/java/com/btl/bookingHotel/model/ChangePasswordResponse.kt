package com.btl.bookingHotel.model

data class ChangePasswordResponse(
    val message: String,
    val status: String
)

data class ChangePasswordRequest(
    val current_password: String,
    val new_password: String
)

