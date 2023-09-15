package com.shoppa.features.products.data

import com.slack.eithernet.ApiResult

interface ProductsRepository {
    suspend fun getAllProducts(): ApiResult<List<ProductDTO>, Any>
}
