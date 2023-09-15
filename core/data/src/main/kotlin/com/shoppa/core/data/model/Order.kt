package com.shoppa.core.data.model

data class Order(
    val id: String,
    val image: String,
    val name: String,
    val description: String,
    val price: Int,
    val currencyCode: String,
    val currencySymbol: String,
    val quantity: Int,
    val status: String
)
