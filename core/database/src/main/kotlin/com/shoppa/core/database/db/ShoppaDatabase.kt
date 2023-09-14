package com.shoppa.core.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shoppa.core.database.dao.CartDao
import com.shoppa.core.database.model.CartEntity

@Database(
    version = 1,
    entities = [CartEntity::class],
    exportSchema = false
)
abstract class ShoppaDatabase : RoomDatabase() {
    abstract fun cartDao() : CartDao
}