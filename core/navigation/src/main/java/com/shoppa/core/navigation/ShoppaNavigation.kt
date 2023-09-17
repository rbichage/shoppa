package com.shoppa.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface ShoppaNavigation {
    fun registerGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    )
}

enum class FeatureDestinations(
    val route: String
) {
    PRODUCTS("products")
}
