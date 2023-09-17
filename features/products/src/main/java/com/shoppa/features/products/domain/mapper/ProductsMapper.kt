package com.shoppa.features.products.domain.mapper

import com.shoppa.core.data.model.Product
import com.shoppa.core.networking.util.BaseResult
import com.shoppa.features.products.data.ErrorResultResponse
import com.shoppa.features.products.data.ProductDTO
import com.shoppa.features.products.domain.usecase.GetProductsUseCase
import com.slack.eithernet.ApiResult
import javax.inject.Inject

interface ProductsMapper {
    fun mapResult(
        result: ApiResult<List<ProductDTO>, ErrorResultResponse>
    ): BaseResult<GetProductsUseCase.Products, GetProductsUseCase.Errors>
}

class ProductsMapperImpl @Inject constructor() : ProductsMapper {
    override fun mapResult(
        result: ApiResult<List<ProductDTO>, ErrorResultResponse>
    ): BaseResult<GetProductsUseCase.Products, GetProductsUseCase.Errors> {
        return when (result) {
            is ApiResult.Success -> {
                val data = result.value

                val mapped = data.map { dto ->
                    Product(
                        id = dto.id.toString(),
                        image = dto.imageLocation,
                        name = dto.name,
                        description = dto.description,
                        price = dto.price,
                        currencyCode = dto.currencyCode,
                        currencySymbol = dto.currencySymbol,
                        quantity = dto.quantity,
                        status = dto.status
                    )
                }

                BaseResult.Success(GetProductsUseCase.Products(mapped))
            }

            is ApiResult.Failure.NetworkFailure -> {
                BaseResult.Failure(GetProductsUseCase.Errors.NetworkError)
            }

            is ApiResult.Failure.UnknownFailure -> {
                BaseResult.Failure(GetProductsUseCase.Errors.UnknownError)
            }

            is ApiResult.Failure.HttpFailure -> {
                val message = result.error?.message ?: result.code.toString()
                BaseResult.Failure(GetProductsUseCase.Errors.HttpError(message))
            }

            is ApiResult.Failure.ApiFailure -> {
                val message = result.error?.message.orEmpty()
                BaseResult.Failure(GetProductsUseCase.Errors.HttpError(message))
            }
        }
    }
}
