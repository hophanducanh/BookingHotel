package com.btl.bookingHotel.model

data class BookingRequest(
    val check_in: String,
    val check_out: String,
    val number_of_children: Int,
    val number_of_people: Int,
    val number_of_rooms: Int = 1,
    val room_id: Int,
    val user_discount_id: Int ?= null
)