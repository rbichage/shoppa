package com.shoppa.core.database.di

import android.content.Context
import androidx.room.Room
import com.shoppa.core.database.dao.CartDao
import com.shoppa.core.database.data.CartRepository
import com.shoppa.core.database.data.CartRepositoryImpl
import com.shoppa.core.database.db.ShoppaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideShoppaDatabase(
        @ApplicationContext context: Context
    ): ShoppaDatabase = Room.databaseBuilder(
        context = context,
        name = "shoppa.db",
        klass = ShoppaDatabase::class.java
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideCartDao(
        shoppaDatabase: ShoppaDatabase
    ): CartDao = shoppaDatabase.cartDao()

    @Provides
    @Singleton
    fun provideCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository = cartRepositoryImpl
}