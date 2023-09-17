package com.shoppa.features.products.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.shoppa.core.data.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    modifier: Modifier = Modifier,
    product: Product,
    onDismiss: () -> Unit
) {
    val viewModel = hiltViewModel<ProductDetailsViewModel>()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()

    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true }
    )
    val config = LocalConfiguration.current
    val height = config.screenHeightDp
    val actualHeight = (height * .85).toInt()

    val cardHeight = (actualHeight * .4).toInt().dp

    ModalBottomSheet(
        modifier = modifier
            .height(height = actualHeight.dp),
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
        tonalElevation = 16.dp,
        dragHandle = null
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .height(cardHeight),
                shape = RoundedCornerShape(size = 16.dp)
            ) {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
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
                        modifier = modifier
                            .size(150.dp)
                    )
                }
            }

            Spacer(modifier = modifier.height(16.dp))

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = modifier.weight(.7F),
                        text = product.name,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        modifier = modifier.weight(.3F),
                        text = "${product.currencySymbol}${product.price}",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.End
                    )
                }

                Spacer(modifier = modifier.height(24.dp))

                Text(
                    text = "Description",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = modifier.height(8.dp))
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Light
                    )
                )

                Spacer(modifier = modifier.height(24.dp))

                ButtonsContent(
                    modifier = modifier,
                    cartItems = cartItems,
                    product = product,
                    onAddToCartClicked = {
                        viewModel.addToCart(product)
                    }
                )
            }
        }
    }
}

@Composable
fun ButtonsContent(
    modifier: Modifier = Modifier,
    cartItems: List<Product>,
    product: Product,
    onAddToCartClicked: () -> Unit
) {
    val isInCart = cartItems.any { it.id == product.id }

    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onAddToCartClicked,
        enabled = !isInCart
    ) {
        Text(text = if (isInCart) "Added to cart" else "Add to cart")
    }
    Spacer(modifier = modifier.height(8.dp))

    OutlinedButton(
        modifier = modifier.fillMaxWidth(),
        onClick = { }
    ) {
        Text(text = "Buy now")
    }
}
