package com.shoppa.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shoppa.core.database.model.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItems(cartEntities: List<CartEntity>)

    @Query("SELECT * FROM cart")
    fun getAllCartItems(): Flow<List<CartEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCartItem(cartEntity: CartEntity)

    @Query("DELETE FROM cart WHERE id=:id")
    suspend fun deleteCartItem(id: String)

    @Query("DELETE FROM cart")
    suspend fun deleteAllCartItems()
}