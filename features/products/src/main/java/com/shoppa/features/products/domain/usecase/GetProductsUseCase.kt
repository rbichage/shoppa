package com.shoppa.features.products.domain.usecase

import com.shoppa.core.data.model.Product
import com.shoppa.core.networking.util.BaseResult
import com.shoppa.features.products.data.repository.ProductsRepository
import com.shoppa.features.products.domain.mapper.ProductsMapper
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val productsMapper: ProductsMapper
) {

    data class Products(
        val items: List<Product>
    )

    sealed interface Errors {
        data object NetworkError : Errors
        data object UnknownError : Errors
        data class HttpError(val message: String) : Errors
    }

    fun execute() = flow {
        emit(BaseResult.Loading())
        val result = productsRepository.getAllProducts()
        emit(productsMapper.mapResult(result))
    }.catch {
        emit(
            BaseResult.Failure(
                Errors.UnknownError
            )
        )
    }
}
