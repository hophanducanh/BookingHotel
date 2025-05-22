package com.btl.bookingHotel.model

import java.io.Serializable

data class BookingResponse(
    val data: BookingData,
    val message: String,
    val status: String
) : Serializable

data class BookingInfo(
    val base_price: Int,
    val check_in: String,
    val check_out: String,
    val created_at: String,
    val discount_applied: Boolean,
    val discount_info: DiscountInfo,
    val num_days: Int,
    val number_of_children: Int,
    val number_of_people: Int,
    val number_of_rooms: Int,
    val total_price: Double
) : Serializable

data class BookingData(
    val booking_id: Int,
    val booking_info: BookingInfo,
    val hotel_info: HotelInfo,
    val room_info: RoomInfo,
    val user_points: Int
) : Serializable

data class DiscountInfo(
    val description: String,
    val discount_id: Int,
    val discount_name: String,
    val discount_value_percent: Double,
    val point_required: Int
) : Serializable

data class HotelInfo(
    val address: String,
    val description: String,
    val hotel_id: Int,
    val hotel_name: String,
    val star: Double
) : Serializable

data class RoomInfo(
    val room_id: Int,
    val room_price: Int,
    val room_type: String
) : Serializable