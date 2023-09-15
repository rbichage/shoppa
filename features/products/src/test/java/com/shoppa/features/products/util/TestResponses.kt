package com.shoppa.features.products.util

val productsJson = """
 [
   {
     "id": 11,
     "name": "10 Lives",
     "description": "10 Lives product bundle.",
     "price": 1,
     "currencyCode": "USD",
     "currencySymbol": "${'$'}",
     "quantity": 10,
     "imageLocation": "https://dev-images-carry1st-products.s3.eu-west-2.amazonaws.com/74e517a3-0615-4019-bb08-cc697cf4e747.png",
     "status": "ACTIVE"
   },
   {
     "id": 19,
     "name": "25 Lives",
     "description": "25 Lives product bundle.",
     "price": 2,
     "currencyCode": "USD",
     "currencySymbol": "${'$'}",
     "quantity": 25,
     "imageLocation": "https://dev-images-carry1st-products.s3.eu-west-2.amazonaws.com/0a90f406-7340-4957-87ae-a8233d8c9f68.png",
     "status": "ACTIVE"
   },
   {
     "id": 20,
     "name": "50 Lives",
     "description": "50 Lives product bundle.",
     "price": 2,
     "currencyCode": "USD",
     "currencySymbol": "${'$'}",
     "quantity": 50,
     "imageLocation": "https://dev-images-carry1st-products.s3.eu-west-2.amazonaws.com/a3fc8953-ba5b-4fb4-9017-223557716594.png",
     "status": "ACTIVE"
   },
   {
     "id": 21,
     "name": "125 Lives",
     "description": "125 Lives product bundle.",
     "price": 5,
     "currencyCode": "USD",
     "currencySymbol": "${'$'}",
     "quantity": 125,
     "imageLocation": "https://dev-images-carry1st-products.s3.eu-west-2.amazonaws.com/ce328218-93b2-4548-ba25-71a49c0b7af3.png",
     "status": "ACTIVE"
   }
 ]
""".trimIndent()

val failureBody = """
    {
    "message": "An error occurred"
    }
""".trimIndent()
