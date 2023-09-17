package com.shoppa.features.products.ui.screens.cart

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shoppa.core.data.model.Product
import com.shoppa.core.data.model.sampleProducts
import com.shoppa.core.design.theme.ShoppaTheme
import com.shoppa.features.products.R
import com.shoppa.features.products.ui.util.UrlImageView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    val viewModel = hiltViewModel<CartViewModel>()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()

    var productUpdate: Product? by remember {
        mutableStateOf(null)
    }

    if (productUpdate != null) {
        QuantityScreen(
            quantity = productUpdate?.maxQuantity ?: 0,
            onQuantityClicked = { quantity ->
                productUpdate?.let { product ->
                    viewModel.updateQuantity(product, quantity)
                }
                productUpdate = null
            },
            onDismiss = {
                productUpdate = null
            }
        )
        LaunchedEffect(key1 = Unit) {
        }
    }

    Column {
        CenterAlignedTopAppBar(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            title = {
                Text(text = stringResource(R.string.your_cart))
            },
            navigationIcon = {
                Column(
                    modifier = modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = null,
                        modifier = modifier.clickable {
                            navigateBack()
                        }
                    )
                }
            }
        )
        CartItemsList(
            products = cartItems,
            onDeleteClicked = {
                viewModel.deleteFromCart(it.id)
            },
            onUpdateClicked = {
                productUpdate = it
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CartItemsList(
    modifier: Modifier = Modifier,
    products: List<Product>,
    onDeleteClicked: (Product) -> Unit,
    onUpdateClicked: (Product) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(products, key = { it.id }) { product ->
            CartItemContent(
                product = product,
                onDeleteClicked = { onDeleteClicked(product) },
                onUpdateClicked = {
                    onUpdateClicked(product)
                }
            )
        }
    }
}

@Preview
@Composable
fun CartItemsPreView() {
    ShoppaTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                CartItemsList(
                    products = sampleProducts,
                    onDeleteClicked = { },
                    onUpdateClicked = {}
                )
            }
        }
    }
}

@Composable
fun CartItemContent(
    modifier: Modifier = Modifier,
    product: Product,
    onDeleteClicked: () -> Unit,
    onUpdateClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Card(
            modifier = modifier
                .weight(.3F)
                .height(120.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(size = 8.dp)
        ) {
            UrlImageView(
                url = product.image,
                imageSize = 80.dp
            )
        }

        Column(
            modifier = modifier
                .weight(.5F)
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 16.dp,
                    bottom = 16.dp
                )
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = modifier.height(8.dp))
            Text(text = stringResource(R.string.quantity, product.quantity))

            TextButton(
                onClick = onUpdateClicked,
                contentPadding = PaddingValues(all = 0.dp)
            ) {
                Text(
                    text = stringResource(R.string.update),
                    textDecoration = TextDecoration.Underline
                )
            }
        }

        Column(
            modifier = modifier
                .height(IntrinsicSize.Max)
                .weight(.2F)
                .padding(start = 8.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "${product.currencySymbol}${product.price}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = modifier.height(36.dp))
            Icon(
                modifier = modifier.clickable {
                    onDeleteClicked()
                },
                imageVector = Icons.Outlined.Delete,
                contentDescription = null
            )
        }
    }
}
