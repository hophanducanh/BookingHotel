package com.btl.bookingHotel.model

data class LocationResponse(
    val data: List<LocationData>,
    val message: String,
    val status: String
)
data class LocationData(
    val city: String,
    val country: String,
    val id_location: Int,
    val name: String
)