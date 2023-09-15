package com.shoppa.features.products.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResultResponse(
    @Json(name = "message")
    val message: String
)
