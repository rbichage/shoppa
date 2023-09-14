package com.shoppa.core.database.data

import com.shoppa.core.database.dao.CartDao
import com.shoppa.core.database.model.CartEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CartRepository {
    fun getAllCartItems(): Flow<List<CartEntity>>
    suspend fun insertCartItem(cartEntities: List<CartEntity>)
    suspend fun updateCartItem(cartEntity: CartEntity)
    suspend fun deleteCartItem(itemId: String)
    suspend fun deleteCartTable()
}

class  CartRepositoryImpl @Inject internal  constructor(
    private val cartDao: CartDao
) : CartRepository{
    override fun getAllCartItems(): Flow<List<CartEntity>> {
        return cartDao.getAllCartItems()
    }

    override suspend fun insertCartItem(cartEntities: List<CartEntity>) {
        cartDao.insertCartItems(cartEntities)
    }

    override suspend fun updateCartItem(cartEntity: CartEntity) {
       cartDao.updateCartItem(cartEntity)
    }

    override suspend fun deleteCartItem(itemId: String) {
       cartDao.deleteCartItem(itemId)
    }

    override suspend fun deleteCartTable() {
        cartDao.deleteAllCartItems()
    }

}