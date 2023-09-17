package com.shoppa.features.products.data.repository

import com.shoppa.features.products.data.ErrorResultResponse
import com.shoppa.features.products.data.ProductDTO
import com.shoppa.features.products.data.api.ProductsApi
import com.slack.eithernet.ApiResult
import javax.inject.Inject

class ProductsRepositoryImpl @Inject internal constructor(
    private val productsApi: ProductsApi
) : ProductsRepository {
    override suspend fun getAllProducts(): ApiResult<List<ProductDTO>, ErrorResultResponse> {
        return productsApi.getAllProducts()
    }
}
