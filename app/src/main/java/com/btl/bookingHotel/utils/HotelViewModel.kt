package com.btl.bookingHotel.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import com.btl.bookingHotel.api.ApiClient
import com.btl.bookingHotel.model.HotelData
import com.btl.bookingHotel.model.HotelResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HotelViewModel(private val context: Context) : ViewModel() {

    fun fetchHotels(
        locationId: Int,
        page: Int? = null,
        perPage: Int? = null,
        sortBy: String? = null,
        sortOrder: String? = null,
        hotelStarMin: Int? = null,
        hotelStarMax: Int? = null,
        newPriceMin: Int? = null,
        newPriceMax: Int? = null,
        userRatingMin: Double? = null,
        hotelName: String? = null,
        address: String? = null,
        hotelStar: Int? = null,
        paging: Boolean? = null,
        onSuccess: ((List<HotelData>) -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        ApiClient.create(context).getHotels(
            locationId, page, perPage, sortBy, sortOrder,
            hotelStarMin, hotelStarMax, newPriceMin, newPriceMax,
            userRatingMin, hotelName, address, hotelStar, paging
        ).enqueue(object : Callback<HotelResponse> {
            override fun onResponse(call: Call<HotelResponse>, response: Response<HotelResponse>) {
                val body = response.body()
                if (response.isSuccessful && body?.status == "success") {
                    onSuccess?.invoke(body.data ?: emptyList())
                } else {
                    onError?.invoke("Lỗi phản hồi: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<HotelResponse>, t: Throwable) {
                onError?.invoke("Lỗi: ${t.message}")
            }
        })
    }
}

