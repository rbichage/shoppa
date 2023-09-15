package com.shoppa.features.products.data
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductDTO(
    @Json(name = "currencyCode")
    val currencyCode: String,
    @Json(name = "currencySymbol")
    val currencySymbol: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "imageLocation")
    val imageLocation: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "price")
    val price: Int,
    @Json(name = "quantity")
    val quantity: Int,
    @Json(name = "status")
    val status: String
)
