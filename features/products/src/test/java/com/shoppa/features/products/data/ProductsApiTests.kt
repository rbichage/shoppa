package com.shoppa.features.products.data

import com.shoppa.features.products.data.api.ProductsApi
import com.shoppa.features.products.util.failureBody
import com.shoppa.features.products.util.productsJson
import com.slack.eithernet.ApiResult
import com.slack.eithernet.ApiResultCallAdapterFactory
import com.slack.eithernet.ApiResultConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlin.test.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ProductsApiTests {

    private val mockWebServer = MockWebServer()
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var api: ProductsApi

    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        ).build()

    private val moshi: Moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Before
    fun setup() {
        mockWebServer.start()
        api = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(mockWebServer.url("/"))
            .addCallAdapterFactory(ApiResultCallAdapterFactory)
            .addConverterFactory(ApiResultConverterFactory)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ProductsApi::class.java)

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        Dispatchers.resetMain()
    }

    @Test
    fun `test getting products is successful`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(productsJson)
        )
        val response = api.getAllProducts()

        val type = Types.newParameterizedType(List::class.java, ProductDTO::class.java)
        val items = moshi.adapter<List<ProductDTO>>(type).fromJson(productsJson)
        val success = response as? ApiResult.Success
        assert(success is ApiResult.Success)
        assertEquals(items, success?.value)
    }

    @Test
    fun `test getting products returns a failure`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(403)
                .setBody(failureBody)
        )
        val response = api.getAllProducts()

        val body = moshi.adapter(ErrorResultResponse::class.java).fromJson(failureBody)
        val failure = response as? ApiResult.Failure
        assert(failure is ApiResult.Failure)
        assert(body != null)
    }
}
