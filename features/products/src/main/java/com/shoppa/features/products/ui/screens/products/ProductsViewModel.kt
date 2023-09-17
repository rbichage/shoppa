package com.shoppa.features.products.ui.screens.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoppa.core.data.model.Product
import com.shoppa.core.database.data.CartRepository
import com.shoppa.core.networking.di.IODispatcher
import com.shoppa.core.networking.util.BaseResult
import com.shoppa.features.products.domain.usecase.GetProductsUseCase
import com.shoppa.features.products.ui.util.toCartItem
import com.shoppa.features.products.ui.util.toProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val cartRepository: CartRepository,
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val getProductsEvent = Channel<Unit>(capacity = Channel.CONFLATED)

    init {
        getProducts()
    }

    val uiState: StateFlow<ProductsUIState> = getProductsEvent
        .receiveAsFlow()
        .flatMapMerge {
            getProductsUseCase.execute()
        }.map { result ->
            mapResultToUiState(result)
        }.flowOn(coroutineDispatcher)
        .stateIn(
            scope = viewModelScope,
            initialValue = ProductsUIState.Idle,
            started = SharingStarted.WhileSubscribed(5000L)
        )

    val cartItems = cartRepository
        .getAllCartItems()
        .map { items ->
            items.map { it.toProduct() }
        }
        .flowOn(coroutineDispatcher)

    fun getProducts() {
        getProductsEvent.trySend(Unit)
    }
    fun addToCart(product: Product) {
        viewModelScope.launch(coroutineDispatcher) {
            val entity = product.toCartItem()
            cartRepository.insertCartItem(listOf(entity))
        }
    }

    private fun mapResultToUiState(
        result: BaseResult<GetProductsUseCase.Products, GetProductsUseCase.Errors>
    ): ProductsUIState {
        return when (result) {
            is BaseResult.Failure -> {
                when (val error = result.error) {
                    is GetProductsUseCase.Errors.HttpError -> {
                        ProductsUIState.Error(
                            errorType = ErrorType.HttpError(
                                error.message
                            )
                        )
                    }

                    GetProductsUseCase.Errors.NetworkError -> {
                        ProductsUIState.Error(
                            errorType = ErrorType.NetworkError
                        )
                    }

                    GetProductsUseCase.Errors.UnknownError -> {
                        ProductsUIState.Error(
                            errorType = ErrorType.UnknownError
                        )
                    }
                }
            }

            is BaseResult.Loading -> ProductsUIState.Loading
            is BaseResult.Success -> ProductsUIState.Success(result.data.items)
        }
    }
}

sealed interface ProductsUIState {
    data object Idle : ProductsUIState
    data object Loading : ProductsUIState
    data class Success(val products: List<Product>) : ProductsUIState
    data class Error(val errorType: ErrorType) : ProductsUIState
}

sealed interface ErrorType {
    data object NetworkError : ErrorType
    data object UnknownError : ErrorType
    data class HttpError(val message: String) : ErrorType
}
