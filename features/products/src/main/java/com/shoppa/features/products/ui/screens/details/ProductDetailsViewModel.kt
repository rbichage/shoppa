package com.shoppa.features.products.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoppa.core.data.model.Product
import com.shoppa.core.database.data.CartRepository
import com.shoppa.core.networking.di.IODispatcher
import com.shoppa.features.products.ui.util.toCartItem
import com.shoppa.features.products.ui.util.toProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {
    val cartItems = cartRepository.getAllCartItems()
        .map { entities ->
            entities.map {
                it.toProduct()
            }
        }.flowOn(coroutineDispatcher)
        .stateIn(
            scope = viewModelScope,
            initialValue = emptyList(),
            started = SharingStarted.WhileSubscribed()
        )

    fun addToCart(product: Product) {
        viewModelScope.launch(coroutineDispatcher) {
            cartRepository.insertCartItem(
                listOf(
                    product.toCartItem()
                )
            )
        }
    }
}
