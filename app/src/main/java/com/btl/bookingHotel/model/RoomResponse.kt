package com.btl.bookingHotel.model

data class RoomResponse(
    val data: List<RoomData>,
    val message: String,
    val status: String
)

data class RoomData(
    val hotel_id: Int,
    val id_hotel_room: Int,
    val room_number: String,
    val room_type: String
)