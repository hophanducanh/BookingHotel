package com.btl.bookingHotel.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val status: String,
    val message: String,
    val data: LoginUserData
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginUserData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("point")
    val point: Int,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone_number")
    val phone_number: String,
    @SerializedName("user_number")
    val user_name: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("avatar_url")
    val avatar_url: String,
    @SerializedName("access_token")
    val access_token: String
)