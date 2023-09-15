package com.shoppa.features.products.data

import com.slack.eithernet.ApiResult
import com.slack.eithernet.DecodeErrorBody
import retrofit2.http.GET

interface ProductsApi {
    @GET("productBundles")
    @DecodeErrorBody
    suspend fun getAllProducts(): ApiResult<List<ProductDTO>, ErrorResultResponse>
}
