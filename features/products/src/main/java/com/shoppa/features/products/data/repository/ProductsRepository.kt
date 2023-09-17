package com.shoppa.features.products.data.repository

import com.shoppa.features.products.data.ErrorResultResponse
import com.shoppa.features.products.data.ProductDTO
import com.slack.eithernet.ApiResult

interface ProductsRepository {
    suspend fun getAllProducts(): ApiResult<List<ProductDTO>, ErrorResultResponse>
}
