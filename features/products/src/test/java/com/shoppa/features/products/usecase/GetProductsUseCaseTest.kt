package com.shoppa.features.products.usecase

import app.cash.turbine.test
import com.hannesdorfmann.instantiator.instance
import com.shoppa.core.networking.util.BaseResult
import com.shoppa.features.products.data.ErrorResultResponse
import com.shoppa.features.products.data.ProductDTO
import com.shoppa.features.products.data.repository.ProductsRepository
import com.shoppa.features.products.domain.mapper.ProductsMapper
import com.shoppa.features.products.domain.mapper.ProductsMapperImpl
import com.shoppa.features.products.domain.usecase.GetProductsUseCase
import com.slack.eithernet.ApiResult
import io.mockk.coEvery
import io.mockk.mockk
import java.io.IOException
import kotlin.test.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetProductsUseCaseTest {

    private lateinit var getProductsUseCase: GetProductsUseCase
    private val testDispatcher = StandardTestDispatcher()

    private val productsMapper: ProductsMapper = ProductsMapperImpl()
    private val productsRepository = mockk<ProductsRepository>()

    private val productDTOs = List(5) {
        instance<ProductDTO>()
    }

    @Before
    fun setUp() {
        getProductsUseCase = GetProductsUseCase(
            productsRepository = productsRepository,
            productsMapper = productsMapper
        )
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getting products is successful`() = runTest {
        val result = ApiResult.success(productDTOs)

        coEvery {
            productsRepository.getAllProducts()
        } returns result

        getProductsUseCase.execute().test {
            assert(awaitItem() is BaseResult.Loading)
            awaitItem().apply {
                val outcome = this as? BaseResult.Success
                val products = outcome?.data?.items.orEmpty()
                assert(outcome is BaseResult.Success)
                assert(products.isNotEmpty())
                assert(products.size == productDTOs.size)

                val randomProduct = products.random()

                assertEquals(
                    productDTOs.any {
                        it.id.toString() == randomProduct.id
                    },
                    true
                )
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test getting products returns a network failure`() = runTest {
        val exception = IOException("Unable to connect")
        val result = ApiResult.networkFailure(exception)

        coEvery {
            productsRepository.getAllProducts()
        } returns result

        getProductsUseCase.execute().test {
            assert(awaitItem() is BaseResult.Loading)
            awaitItem().apply {
                val outcome = this as? BaseResult.Failure
                assert(outcome is BaseResult.Failure)
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test getting products returns a http failure`() = runTest {
        val errorCode = 401
        val error = ErrorResultResponse("Unauthorized!")
        val result = ApiResult.httpFailure(
            code = errorCode,
            error = error
        )

        coEvery {
            productsRepository.getAllProducts()
        } returns result

        getProductsUseCase.execute().test {
            assert(awaitItem() is BaseResult.Loading)
            awaitItem().apply {
                val outcome = this as? BaseResult.Failure
                val errorState = outcome?.error as? GetProductsUseCase.Errors.HttpError
                assert(outcome is BaseResult.Failure)
                assert(errorState is GetProductsUseCase.Errors.HttpError)
                assert(errorState?.message == error.message)
            }

            cancelAndIgnoreRemainingEvents()
        }
    }
}
