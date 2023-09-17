package com.shoppa.features.products.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.shoppa.core.navigation.FeatureDestinations
import com.shoppa.features.products.ui.screens.cart.CartScreen
import com.shoppa.features.products.ui.screens.products.ProductsScreen
import javax.inject.Inject

class ProductsNavigationImpl @Inject constructor() : ProductsNavigation {
    override fun registerGraph(navController: NavController, navGraphBuilder: NavGraphBuilder) {
        ProductsNavigationObject.registerGraph(navController, navGraphBuilder)
    }
}

internal object ProductsNavigationObject {
    fun registerGraph(navController: NavController, navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.navigation(
            route = FeatureDestinations.PRODUCTS.route,
            startDestination = ProductsDestination.ProductsScreen.route
        ) {
            composable(
                route = ProductsDestination.ProductsScreen.route
            ) {
                ProductsScreen(
                    navigateToCart = {
                        navController.navigate(ProductsDestination.CartScreen.route)
                    }
                )
            }
            composable(
                route = ProductsDestination.CartScreen.route
            ) {
                CartScreen {
                    navController.navigateUp()
                }
            }
        }
    }
}

sealed class ProductsDestination(val route: String) {
    data object ProductsScreen : ProductsDestination("all")
    data object CartScreen : ProductsDestination("cart")
}
