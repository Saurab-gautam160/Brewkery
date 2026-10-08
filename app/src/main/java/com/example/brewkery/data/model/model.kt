package com.example.brewkery.data.model

import com.google.gson.annotations.SerializedName

data class StoreConfig(
    @SerializedName("status") val status: String = "Open",
    @SerializedName("deliveryEstimate") val deliveryEstimate: String = "20 – 30 mins",
    @SerializedName("flatFee") val flatFee: Double = 2.50,
    @SerializedName("taxRate") val taxRate: Double = 0.08
)

data class CustomizationOption(
    @SerializedName("name") val name: String = "",
    @SerializedName("priceModifier") val priceModifier: Double = 0.0
)

data class MenuItem(
    @SerializedName("id", alternate = ["item_id"])
    val id: String = "",

    @SerializedName("name", alternate = ["title"])
    val name: String = "",

    @SerializedName("badge")
    val badge: String? = null,

    @SerializedName("description")
    val description: String = "",

    @SerializedName("price", alternate = ["item_price"])
    val price: Double = 0.0,

    @SerializedName("rating")
    val rating: Double = 0.0,

    @SerializedName("ratingCount", alternate = ["rating_count"])
    val ratingCount: Int = 0,

    @SerializedName("category")
    val category: String = "",

    @SerializedName("imageUrl", alternate = ["image_url", "image"])
    val imageUrl: String = "",

    @SerializedName("ingredients")
    val ingredients: List<String>? = emptyList(),

    @SerializedName("sizes", alternate = ["size_options"])
    val sizes: List<CustomizationOption>? = emptyList(),

    @SerializedName("milkOptions", alternate = ["milk_options"])
    val milkOptions: List<CustomizationOption>? = emptyList(),

    @SerializedName("sugarLevels", alternate = ["sugar_levels"])
    val sugarLevels: List<CustomizationOption>? = emptyList()
)

data class MenuResponse(
    @SerializedName("store") val store: StoreConfig?,
    @SerializedName("categories") val categories: List<Category>?,
    @SerializedName("items") val items: List<MenuItem> = emptyList()
)

data class Category(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String = "",
    @SerializedName("icon") val icon: String? = null
)

data class CartItem(
    val item: MenuItem,
    val selectedSize: CustomizationOption? = null,
    val selectedMilk: CustomizationOption? = null,
    val selectedSugar: CustomizationOption? = null,
    val quantity: Int = 1
) {
    val unitPrice: Double
        get() = item.price +
                (selectedSize?.priceModifier ?: 0.0) +
                (selectedMilk?.priceModifier ?: 0.0) +
                (selectedSugar?.priceModifier ?: 0.0)

    val totalPrice: Double
        get() = unitPrice * quantity
}