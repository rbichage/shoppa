package com.shoppa.features.products.data

import com.shoppa.features.products.util.productsJson
import com.slack.eithernet.ApiResult
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.coEvery
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProductsRepositoryImplTest {

    private var productsApi = mockk<ProductsApi>(relaxed = true)
    private lateinit var productsRepository: ProductsRepository

    private val dispatcher = UnconfinedTestDispatcher()

    private val moshi = Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private var dtos = listOf<ProductDTO>()
    private val type = Types.newParameterizedType(List::class.java, ProductDTO::class.java)

    private val codeAndError = Pair(401, "Oops, an error occurred")

    private val adapter = moshi.adapter<List<ProductDTO>>(type)

    private val exception = IOException("Unable to connect")

    @Before
    fun setUp() {
        productsRepository = ProductsRepositoryImpl(
            productsApi = productsApi
        )
        dtos = adapter.fromJson(productsJson).orEmpty()
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getting all products is successful`() = runTest {
        val response = ApiResult.success(dtos)

        coEvery {
            productsApi.getAllProducts()
        } returns response

        (response as? ApiResult.Success).apply {
            assert(this is ApiResult.Success)
            val data = this?.value.orEmpty()

            val randomItem = data.random()

            assert(data.size == dtos.size)
            assertEquals(dtos.firstOrNull { it.id == randomItem.id }?.name, randomItem.name)
        }
    }

    @Test
    fun `test getting all products returns a network failure`() = runTest {
        val response = ApiResult.networkFailure(exception)

        coEvery {
            productsApi.getAllProducts()
        } returns response

        (response as? ApiResult.Failure.NetworkFailure).apply {
            assert(this is ApiResult.Failure.NetworkFailure)
            val data = this?.error
            assert(data is IOException)
            assert(data?.message == exception.message)
        }
    }
}
