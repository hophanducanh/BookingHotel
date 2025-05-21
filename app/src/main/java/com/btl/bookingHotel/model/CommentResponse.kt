package com.btl.bookingHotel.model

data class CommentResponse(
    val data: CommentData,
    val message: String,
    val status: String
)

data class CommentRequest(
    val status: String,
    val message: String,
    val data: Comment
)

data class Comment(
    val comment: String,
    val created_at: String,
    val hotel_id: Int,
    val id_comment: Int,
    val images: List<String>,
    val rating_point: Double,
    val user: CommentUser
)
data class CommentData(
    val comments: List<Comment>,
    val hotel: CommentHotel
)

data class CommentHotel(
    val address: String,
    val description: String,
    val facilities: List<String>,
    val hotel_name: String,
    val hotel_rating: Double,
    val hotel_star: Double,
    val images: List<String>,
    val location: CommentLocation
)

data class CommentUser(
    val avatar_url: String,
    val email: String,
    val id: Int,
    val user_name: String
)

data class CommentLocation(
    val city: String,
    val country: String
)