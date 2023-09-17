package com.shoppa.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.shoppa.core.navigation.FeatureDestinations
import com.shoppa.features.products.navigation.ProductsNavigation

@Composable
fun ShoppaNavHost(
    navHostController: NavHostController,
    navigationProvider: NavigationProvider
) {
    NavHost(
        navController =navHostController,
        startDestination = FeatureDestinations.PRODUCTS.route
    ) {
        navigationProvider.productNavigation.registerGraph(
            navHostController, this
        )
    }
}

/**
 *  THis data class contains all the feature nested navigations.
 *
 * Remember to include your navigation on this file and inject it on [Navigation Module]
 *
 * */
data class NavigationProvider(
    val productNavigation: ProductsNavigation,
)