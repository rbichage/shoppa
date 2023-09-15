package com.shoppa.features.products.di

import com.shoppa.features.products.data.ProductsApi
import com.shoppa.features.products.data.ProductsRepository
import com.shoppa.features.products.data.ProductsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ProductsModule {
    @Singleton
    @Provides
    fun provideProductsRetrofit(
        builder: Retrofit.Builder
    ): ProductsApi {
        return builder
            .build()
            .create(ProductsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideProductsRepository(
        productsRepositoryImpl: ProductsRepositoryImpl
    ): ProductsRepository {
        return productsRepositoryImpl
    }
}
