package com.shoppa.core.data.model

data class Product(
    val id: String,
    val image: String,
    val name: String,
    val description: String,
    val price: Int,
    val currencyCode: String,
    val currencySymbol: String,
    val quantity: Int,
    val maxQuantity: Int = quantity,
    val status: String
)

val sampleProducts = listOf(
    Product(
        id = "11",
        image = "https://dev-images-carry1st-products.s3.eu-west-2.amazonaws.com/74e517a3-0615-4019-bb08-cc697cf4e747.png",
        name = "10 Lives",
        description = "10 Lives product bundle.",
        price = 1,
        currencyCode = "USD",
        currencySymbol = "$",
        quantity = 10,
        status = "ACTIVE"
    ),
    Product(
        id = "19",
        image = "https://dev-images-carry1st-products.s3.eu-west-2.amazonaws.com/0a90f406-7340-4957-87ae-a8233d8c9f68.png",
        name = "25 Lives",
        description = "25 Lives product bundle .",
        price = 2,
        currencyCode = "USD",
        currencySymbol = "$", quantity = 25, status = "ACTIVE"
    ),
    Product(
        id = "20",
        image = "https://dev-images-carry1st-products.s3.eu-west-2.amazonaws.com/a3fc8953-ba5b-4fb4-9017-223557716594.png",
        name = "150 Lives",
        description = "150 Lives product bundle.",
        price = 2,
        currencyCode = "USD",
        currencySymbol = "$",
        quantity = 50,
        status = "ACTIVE"
    ),
    Product(
        id = "21",
        image = "https://dev-images-carry1st-products.s3.eu-west-2.amazonaws.com/ce328218-93b2-4548-ba25-71a49c0b7af3.png",
        name = "125 Lives",
        description = "125 Lives product bundle.",
        price = 5,
        currencyCode = "USD",
        currencySymbol = "$",
        quantity = 125,
        status = "ACTIVE"
    ),
    Product(
        id = "22",
        image = "https://dev-images-carry1st-products.s3.eu-west-2.amazonaws.com/c3593c7e-da28-4013-87eb-d06582f60bc1.png",
        name = "500 Lives",
        description = "500 Lives product bundle.",
        price = 10,
        currencyCode = "USD",
        currencySymbol = "$",
        quantity = 500,
        status = "ACTIVE"
    ),

    )
