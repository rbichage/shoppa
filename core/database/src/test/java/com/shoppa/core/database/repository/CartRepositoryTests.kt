package com.shoppa.core.database.repository

import app.cash.turbine.test
import com.hannesdorfmann.instantiator.InstantiatorConfig
import com.hannesdorfmann.instantiator.instance
import com.shoppa.core.database.dao.CartDao
import com.shoppa.core.database.data.CartRepository
import com.shoppa.core.database.data.CartRepositoryImpl
import com.shoppa.core.database.model.CartEntity
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class CartRepositoryTests {
    private val dispatcher = UnconfinedTestDispatcher()
    private val config = InstantiatorConfig(useNull = false, useDefaultArguments = false)

    val cartEntities = List(3) {
        instance<CartEntity>(config)
    }

    private val cartDao = mockk<CartDao>(relaxed = true)
    private lateinit var cartRepository: CartRepository

    @Before
    fun setup() {
        cartRepository = CartRepositoryImpl(
            cartDao = cartDao
        )
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `test inserting and retrieving items`() = runTest {
        coEvery {
            cartDao.getAllCartItems()
        } returns flowOf(cartEntities)

        cartRepository.insertCartItem(cartEntities)

        cartRepository.getAllCartItems().test {
            val list = awaitItem()

            assert(list.size == cartEntities.size)
            assert(
                cartEntities.firstOrNull { it.productId == cartEntities.first().productId } != null
            )
            cancelAndIgnoreRemainingEvents()
        }
    }
}
