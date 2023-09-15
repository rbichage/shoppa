package com.shoppa.features.products.data

import com.slack.eithernet.ApiResult
import javax.inject.Inject

class ProductsRepositoryImpl @Inject internal constructor(
    private val productsApi: ProductsApi
) : ProductsRepository {
    override suspend fun getAllProducts(): ApiResult<List<ProductDTO>, Any> {
        return productsApi.getAllProducts()
    }
}
