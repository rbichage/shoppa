package com.shoppa.features.products.ui.screens.details

import app.cash.turbine.test
import com.hannesdorfmann.instantiator.instance
import com.shoppa.core.data.model.Product
import com.shoppa.core.database.data.CartRepository
import com.shoppa.core.database.model.CartEntity
import com.shoppa.features.products.ui.util.toCartItem
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class ProductDetailsViewModelTest {

    private val cartRepository: CartRepository = mockk(relaxed = true)
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var productDetailsViewModel: ProductDetailsViewModel
    private val product = instance<Product>()

    @Before
    fun setUp() {
        productDetailsViewModel = ProductDetailsViewModel(
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
    fun `test adding to cart returns the added item`() = runTest {
        val cartItems = MutableStateFlow(listOf<CartEntity>())

        coEvery {
            cartRepository.getAllCartItems()
        } returns cartItems

        productDetailsViewModel.addToCart(product)

        assert(productDetailsViewModel.cartItems.first().isEmpty())
        cartItems.emit(
            listOf(product.toCartItem())
        )
        productDetailsViewModel.getCartItems()
        productDetailsViewModel.cartItems.test {
            val list = awaitItem()
            assert(list.isNotEmpty())
            assertEquals(list.any { it.id == product.id }, true)
        }
    }
}
