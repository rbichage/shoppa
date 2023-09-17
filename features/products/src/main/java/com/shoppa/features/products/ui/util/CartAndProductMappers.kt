package com.shoppa.features.products.ui.util

import com.shoppa.core.data.model.Product
import com.shoppa.core.database.model.CartEntity

fun Product.toCartItem(selectedQuantity: Int = 1): CartEntity {
    return CartEntity(
        productId = id,
        productName = name,
        description = description,
        imageUrl = image,
        price = price,
        currencyCode = currencyCode,
        maxQuantity = maxQuantity,
        selectedQuantity = selectedQuantity,
        currencySymbol = currencySymbol
    )
}

fun CartEntity.toProduct(): Product {
    return Product(
        id = productId,
        name = productName,
        description = description,
        image = imageUrl,
        price = price,
        currencyCode = currencyCode,
        quantity = selectedQuantity,
        maxQuantity = maxQuantity,
        currencySymbol = currencySymbol,
        status = ""
    )
}
