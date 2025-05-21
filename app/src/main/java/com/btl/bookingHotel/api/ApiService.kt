package com.btl.bookingHotel.api

import com.btl.bookingHotel.model.BookingRequest
import com.btl.bookingHotel.model.BookingResponse
import com.btl.bookingHotel.model.CommentRequest
import com.btl.bookingHotel.model.CommentResponse
import com.btl.bookingHotel.model.HotelResponse
import com.btl.bookingHotel.model.LocationResponse
import com.btl.bookingHotel.model.LoginRequest
import com.btl.bookingHotel.model.LoginResponse
import com.btl.bookingHotel.model.MyDiscountResponse
import com.btl.bookingHotel.model.RegisterResponse
import com.btl.bookingHotel.model.RoomResponse
import com.btl.bookingHotel.model.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("/register")
    fun registerUser(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
        @Part("user_name") username: RequestBody,
        @Part("country") country: RequestBody,
        @Part avatar: MultipartBody.Part
    ): Call<RegisterResponse>

    @POST("/login")
    fun loginUser(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    @GET("api/locations")
    fun getLocations(): Call<LocationResponse>

    @GET("/hotels/location/{location_id}")
    fun getHotels(
        @Path("location_id") locationId: Int,
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("sort_by") sortBy: String? = null,
        @Query("sort_order") sortOrder: String? = null,
        @Query("hotel_star_min") hotelStarMin: Int? = null,
        @Query("hotel_star_max") hotelStarMax: Int? = null,
        @Query("new_price_min") newPriceMin: Int? = null,
        @Query("new_price_max") newPriceMax: Int? = null,
        @Query("user_rating_min") userRatingMin: Double? = null,
        @Query("hotel_name") hotelName: String? = null,
        @Query("address") address: String? = null,
        @Query("hotel_star") hotelStar: Int? = null,
        @Query("paging") paging: Boolean? = null
    ): Call<HotelResponse>

    @GET("available-rooms")
    fun getAvailableRooms(
        @Query("hotel_id") hotelId: Int? = null,
        @Query("room_type") roomType: String,
        @Query("check_in") checkIn: String,
        @Query("check_out") checkOut: String
    ): Call<RoomResponse>

    @GET("/profile")
    fun getProfile(): Call<UserResponse>

    @GET("/comments/location/{hotel_id}")
    fun getComments(@Path("hotel_id") locationId: Int): Call<CommentResponse>

    @Multipart
    @POST("comments")
    fun postComment(
        @Part("rating_point") ratingPoint: RequestBody,
        @Part("hotel_id") hotelId: RequestBody,
        @Part("comment") comment: RequestBody,
        @Part images: List<MultipartBody.Part>?
    ): Call<CommentRequest>

    @GET("/get-discount")
    fun getMyDiscount(): Call<MyDiscountResponse>

    @POST("bookings")
    fun createBooking(@Body booking: BookingRequest): Call<BookingResponse>
}
