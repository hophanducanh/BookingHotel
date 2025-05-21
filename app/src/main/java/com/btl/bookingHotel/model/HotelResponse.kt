package com.btl.bookingHotel.model
import java.io.Serializable

data class HotelResponse(
    val data: List<HotelData>,
    val message: String,
    val pagination: Pagination,
    val status: String
): Serializable

data class HotelData(
    val address: String,
    val descriptions: String,
    val distance: String,
    val facilities: List<String>,
    val hotel_name: String,
    val hotel_rating: Double,
    val hotel_star: Double,
    val id: Int,
    val image: List<String>,
    val location: Location,
    val new_price: Int,
    val old_price: Int,
    val policies: String,
    val user_rating: Float?
) : Serializable

data class Location(
    val city: String,
    val country: String,
    val id_location: Int,
    val name: String
): Serializable

data class Pagination(
    val current_page: Int? = null,
    val has_next: Boolean? = null,
    val has_prev: Boolean? = null,
    val next_page: Int? = null,
    val per_page: Int? = null,
    val prev_page: Any? = null,
    val total_hotels: Int,
    val total_pages: Int? = null
): Serializable