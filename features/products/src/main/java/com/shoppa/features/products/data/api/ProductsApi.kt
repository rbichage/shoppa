package com.shoppa.features.products.data.api

import com.shoppa.features.products.data.ErrorResultResponse
import com.shoppa.features.products.data.ProductDTO
import com.slack.eithernet.ApiResult
import com.slack.eithernet.DecodeErrorBody
import retrofit2.http.GET

interface ProductsApi {
    @GET("productBundles")
    @DecodeErrorBody
    suspend fun getAllProducts(): ApiResult<List<ProductDTO>, ErrorResultResponse>
}
