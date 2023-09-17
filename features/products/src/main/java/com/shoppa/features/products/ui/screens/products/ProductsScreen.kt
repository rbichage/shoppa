package com.shoppa.features.products.ui.screens.products

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.shoppa.core.data.model.Product
import com.shoppa.core.data.model.sampleProducts
import com.shoppa.core.design.theme.ShoppaTheme
import com.shoppa.features.products.R
import com.shoppa.features.products.ui.screens.details.ProductDetailsScreen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    navigateToCart: () -> Unit
) {
    val viewModel = hiltViewModel<ProductsViewModel>()
    val productsUiState by viewModel.uiState.collectAsStateWithLifecycle()
    var cartItems by remember {
        mutableIntStateOf(0)
    }
    val scrollState = rememberScrollState()
    var lastSelectedProduct: Product? by remember {
        mutableStateOf(null)
    }

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    if (showBottomSheet) {
        lastSelectedProduct?.let { product ->
            ProductDetailsScreen(
                product = product,
                onDismiss = {
                    showBottomSheet = false
                }
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.cartItems.collectLatest {
            cartItems = it.size
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        Column {
            SearchContent()

            Spacer(modifier = modifier.height(16.dp))
            ProductsContent(
                productsUIState = productsUiState,
                scrollState = scrollState,
                onProductClicked = {
                    lastSelectedProduct = it
                    showBottomSheet = true
                },
                onRetryClick = { viewModel.getProducts() }
            )
        }

        if (cartItems > 0) {
            CartContent(
                modifier = modifier.align(Alignment.BottomEnd)
            ) {
                navigateToCart()
            }
        }
    }
}

@Composable
fun SearchContent(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(percent = 50)

    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
            Text(
                text = stringResource(R.string.tap_to_search),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun CartContent(
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(bottom = 8.dp)
    ) {
        ExtendedFloatingActionButton(
            onClick = onSearchClick
        ) {
            Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = null)
            Spacer(modifier = modifier.height(8.dp))
            Text(text = stringResource(R.string.view_cart))
        }
    }
}

@Composable
fun ErrorContent(
    modifier: Modifier = Modifier,
    message: String,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
    ) {
        Text(
            text = message,
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = modifier.width(32.dp))

        Button(onClick = {
            onRetryClick()
        }) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Composable
fun ProductsContent(
    modifier: Modifier = Modifier,
    productsUIState: ProductsUIState,
    scrollState: ScrollState,
    onProductClicked: (Product) -> Unit,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        when (productsUIState) {
            is ProductsUIState.Error -> {
                when (val errorTye = productsUIState.errorType) {
                    is ErrorType.HttpError -> {
                        ErrorContent(message = errorTye.message) {
                            onRetryClick()
                        }
                    }

                    ErrorType.NetworkError, ErrorType.UnknownError -> {
                        ErrorContent(message = stringResource(R.string.network_error_message)) {
                            onRetryClick()
                        }
                    }
                }
            }

            ProductsUIState.Idle -> {}
            ProductsUIState.Loading -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ProductsUIState.Success -> {
                val products = productsUIState.products
                ProductsList(
                    products = products,
                    scrollState = scrollState,
                    onProductClicked = onProductClicked
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductsList(
    modifier: Modifier = Modifier,
    products: List<Product>,
    scrollState: ScrollState,
    onProductClicked: (Product) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = 16.dp, bottom = 16.dp)
            .animateContentSize()
    ) {
        FlowRow(
            modifier = modifier.fillMaxWidth(),
            maxItemsInEachRow = 2,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            products.forEach { product ->
                Card(
                    modifier = modifier
                        .fillMaxWidth(.48F)
                        .clickable { onProductClicked(product) },
                    shape = RoundedCornerShape(percent = 15)
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(product.image)
                                .crossfade(true)
                                .build(),
                            loading = {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(
                                            height = 24.dp,
                                            width = 24.dp
                                        )
                                    )
                                }
                            },
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = modifier.size(100.dp)
                        )

                        Spacer(modifier = modifier.height(16.dp))
                        Text(
                            text = product.name,
                            modifier = modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = modifier.height(8.dp))
                        Text(
                            text = "${product.currencySymbol}${product.price}",
                            modifier = modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "success")
@Composable
fun ProductsScreenPreview() {
    ShoppaTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    SearchContent()
                    Spacer(modifier = Modifier.height(16.dp))
                    ProductsContent(
                        productsUIState = ProductsUIState.Success(sampleProducts),
                        scrollState = rememberScrollState(),
                        onProductClicked = {},
                        onRetryClick = {}
                    )
                }

                CartContent(
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                }
            }
        }
    }
}
