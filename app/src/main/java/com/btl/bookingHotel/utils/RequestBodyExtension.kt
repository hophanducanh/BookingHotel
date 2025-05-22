package com.btl.bookingHotel.utils


import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun String.toRequestBody(mediaType: MediaType? = "text/plain".toMediaTypeOrNull()): RequestBody {
    return this.toRequestBody(mediaType)
}
