package com.shoppa.app.di

import com.shoppa.app.ui.navigation.NavigationProvider
import com.shoppa.features.products.navigation.ProductsNavigation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    @Provides
    @Singleton
    fun provideNavigationProvider(
        productsNavigation: ProductsNavigation
    ): NavigationProvider{
        return NavigationProvider(
            productNavigation = productsNavigation
        )
    }
}