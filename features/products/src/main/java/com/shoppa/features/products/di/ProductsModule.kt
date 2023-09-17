package com.shoppa.features.products.di

import com.shoppa.features.products.data.api.ProductsApi
import com.shoppa.features.products.data.repository.ProductsRepository
import com.shoppa.features.products.data.repository.ProductsRepositoryImpl
import com.shoppa.features.products.domain.mapper.ProductsMapper
import com.shoppa.features.products.domain.mapper.ProductsMapperImpl
import com.shoppa.features.products.navigation.ProductsNavigation
import com.shoppa.features.products.navigation.ProductsNavigationImpl
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

    @Singleton
    @Provides
    fun provideProductsMapper(
        productsMapperImpl: ProductsMapperImpl
    ): ProductsMapper = productsMapperImpl

    @Singleton
    @Provides
    fun provideProductsNavigation(
        productsNavigationImpl: ProductsNavigationImpl
    ): ProductsNavigation = productsNavigationImpl
}
