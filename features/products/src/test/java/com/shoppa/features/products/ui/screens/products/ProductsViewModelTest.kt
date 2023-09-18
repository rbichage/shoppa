package com.shoppa.features.products.ui.screens.products

import app.cash.turbine.testIn
import com.hannesdorfmann.instantiator.instance
import com.shoppa.core.data.model.Product
import com.shoppa.core.database.data.CartRepository
import com.shoppa.core.networking.util.BaseResult
import com.shoppa.features.products.domain.usecase.GetProductsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class ProductsViewModelTest {

    private lateinit var productsViewModel: ProductsViewModel
    private val getProducts: GetProductsUseCase = mockk(relaxed = true)
    private val cartRepository = mockk<CartRepository>(relaxed = true)
    private val dispatcher = UnconfinedTestDispatcher()

    val products = List(5) {
        instance<Product>()
    }

    @Before
    fun setUp() {
        productsViewModel = ProductsViewModel(
            getProductsUseCase = getProducts,
            cartRepository = cartRepository,
            coroutineDispatcher = dispatcher
        )
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getting products is successful`() = runTest {
        val result =
            MutableStateFlow<BaseResult<GetProductsUseCase.Products, GetProductsUseCase.Errors>>(
                BaseResult.Loading()
            )

        coEvery {
            getProducts.execute()
        } returns result

        val testFlow = productsViewModel.uiState.testIn(this)
        productsViewModel.getProducts()

        assert(testFlow.awaitItem() == ProductsUIState.Loading)

        result.emit(
            BaseResult.Success(
                GetProductsUseCase.Products(products)
            )
        )

        testFlow.apply {
            val uiState = awaitItem() as? ProductsUIState.Success
            val items = uiState?.products.orEmpty()
            assert(uiState is ProductsUIState.Success)
            assert(items.size == products.size)
            val randomItem = items.random()

            assertEquals(products.any { it.id == randomItem.id }, true)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test getting products returns a network error`() = runTest {
        val result =
            MutableStateFlow<BaseResult<GetProductsUseCase.Products, GetProductsUseCase.Errors>>(
                BaseResult.Loading()
            )

        coEvery {
            getProducts.execute()
        } returns result

        val testFlow = productsViewModel.uiState.testIn(this)
        productsViewModel.getProducts()

        assert(testFlow.awaitItem() == ProductsUIState.Loading)

        result.emit(
            BaseResult.Failure(
                GetProductsUseCase.Errors.NetworkError
            )
        )

        testFlow.apply {
            val uiState = awaitItem() as? ProductsUIState.Error
            val errorType = uiState?.errorType as? ErrorType.NetworkError
            assert(uiState is ProductsUIState.Error)
            assertIs<ErrorType.NetworkError>(errorType)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test getting products returns a Http error`() = runTest {
        val message = "Unable to complete your request"
        val result =
            MutableStateFlow<BaseResult<GetProductsUseCase.Products, GetProductsUseCase.Errors>>(
                BaseResult.Loading()
            )

        coEvery {
            getProducts.execute()
        } returns result

        val testFlow = productsViewModel.uiState.testIn(this)
        productsViewModel.getProducts()

        assert(testFlow.awaitItem() == ProductsUIState.Loading)

        result.emit(
            BaseResult.Failure(
                GetProductsUseCase.Errors.HttpError(message)
            )
        )

        testFlow.apply {
            val uiState = awaitItem() as? ProductsUIState.Error
            val errorType = uiState?.errorType as? ErrorType.HttpError
            assert(uiState is ProductsUIState.Error)
            assertIs<ErrorType.HttpError>(errorType)
            assertEquals(errorType.message, message)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
