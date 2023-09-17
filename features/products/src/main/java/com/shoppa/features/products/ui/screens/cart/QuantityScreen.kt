package com.shoppa.features.products.ui.screens.cart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuantityScreen(
    modifier: Modifier = Modifier,
    quantity: Int,
    onQuantityClicked: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetsState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true }
    )
    val list by remember {
        mutableStateOf((1..quantity).toList())
    }

    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(list) { quantity ->
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .clickable {
                            onQuantityClicked(quantity)
                            scope
                                .launch {
                                    sheetsState.hide()
                                }
                                .invokeOnCompletion {
                                    onDismiss()
                                }
                        }
                ) {
                    Text(text = quantity.toString())
                }
            }
        }
    }
}
