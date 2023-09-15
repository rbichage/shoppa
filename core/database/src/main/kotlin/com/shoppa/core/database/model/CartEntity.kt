package com.shoppa.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = false) val productId: String,
    @ColumnInfo(name = "name") val productName: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "currency_code") val currencyCode: String,
    @ColumnInfo(name = "currency_symbol") val currencySymbol: String,
    @ColumnInfo(name = "selected_quantity") val selectedQuantity: Int,
    @ColumnInfo(name = "max_quantity") val maxQuantity: Int,
    @ColumnInfo(name = "image_url") val imageUrl: String,
)