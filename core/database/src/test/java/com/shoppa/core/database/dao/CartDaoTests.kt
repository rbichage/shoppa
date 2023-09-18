package com.shoppa.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.hannesdorfmann.instantiator.instance
import com.shoppa.core.database.db.ShoppaDatabase
import com.shoppa.core.database.model.CartEntity
import java.util.UUID
import kotlin.test.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Config.OLDEST_SDK], manifest = Config.NONE)
class CartDaoTests {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var shoppaDatabase: ShoppaDatabase
    private lateinit var cartDao: CartDao

    private val testProductName = UUID.randomUUID().toString()
    private val testItems = List(5) {
        instance<CartEntity>()
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val context = ApplicationProvider.getApplicationContext<Context>()

        shoppaDatabase = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = ShoppaDatabase::class.java
        ).setQueryExecutor(testDispatcher.asExecutor())
            .allowMainThreadQueries()
            .build()

        cartDao = shoppaDatabase.cartDao()
    }

    @Test
    fun `test inserting cart items returns the items`() = runTest {
        val item = testItems.first()

        cartDao.insertCartItems(testItems)

        val items = cartDao.getAllCartItems().first()

        items.apply {
            val firstItem = random()
            assert(items.size == this.size)
            assertEquals(
                testItems.any {
                    it.productId == firstItem.productId
                },
                true
            )
        }
    }

    @Test
    fun `test deleting item is successful`() = runTest {
        val lastItem = testItems.last()

        cartDao.insertCartItems(testItems)

        cartDao.deleteCartItem(lastItem.productId)

        cartDao.getAllCartItems().test {
            val list = this.awaitItem()
            assertEquals(list.size, testItems.size - 1)
            assertEquals(list.firstOrNull { it.productId == lastItem.productId }, null)
        }
    }

    @Test
    fun `test updating item returns the update result`() = runTest {
        val item = testItems.first()
        val updateItem = item.copy(
            productName = testProductName
        )

        cartDao.insertCartItems(listOf(item))
        val updated = cartDao.updateCartItem(updateItem)

        cartDao.getAllCartItems().test {
            val list = awaitItem()
            assertEquals(list.size, 1)
            assert(list.first().productName == updateItem.productName)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `test deleting cart table items`() = runTest {
        cartDao.insertCartItems(testItems)

        cartDao.deleteAllCartItems()

        cartDao.getAllCartItems().test {
            val list = awaitItem()
            assert(list.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @After
    fun tearDown() {
        shoppaDatabase.close()
        Dispatchers.resetMain()
    }
}
