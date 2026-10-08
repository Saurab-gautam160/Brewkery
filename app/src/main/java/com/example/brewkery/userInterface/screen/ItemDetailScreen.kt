package com.example.brewkery.userInterface.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.brewkery.data.model.CartItem
import com.example.brewkery.userInterface.BrewkeryViewModel
import com.example.brewkery.userInterface.UiState

@Composable
fun ItemDetailScreen(
    itemId: String,
    viewModel: BrewkeryViewModel,
    onBack: () -> Unit
) {
    LaunchedEffect(itemId) {
        viewModel.loadItemDetail(itemId)
    }

    val detailState by viewModel.detailState.collectAsState()
    val warmBg = Color(0xFFFAF7F2)
    val primaryColor = Color(0xFFA03318)
    val lightBorder = Color(0xFFEFE8E0)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(warmBg)
    ) {
        when (val state = detailState) {
            is UiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryColor)
                }
            }
            is UiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.message, color = Color.Red)
                }
            }
            is UiState.Success -> {
                val item = state.data
                var selectedSize by remember(item) { mutableStateOf(item.sizes?.firstOrNull()) }
                var selectedMilk by remember(item) { mutableStateOf(item.milkOptions?.firstOrNull()) }
                var selectedSugar by remember(item) { mutableStateOf(item.sugarLevels?.firstOrNull()) }
                var isFavorite by remember { mutableStateOf(false) }
                var quantity by remember { mutableIntStateOf(1) }

                val unitPrice = item.price +
                        (selectedSize?.priceModifier ?: 0.0) +
                        (selectedMilk?.priceModifier ?: 0.0) +
                        (selectedSugar?.priceModifier ?: 0.0)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(12.dp))

                    // 1. TOP HEADER BAR
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black
                            )
                        }

                        Text(
                            text = "ITEM CUSTOMIZER",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = Color.Black
                        )

                        IconButton(
                            onClick = { isFavorite = !isFavorite },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = Color(0xFFD32F2F)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // IMAGE HEADER
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(item.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = item.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        item.badge?.let { badgeText ->
                            Surface(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .align(Alignment.TopStart),
                                shape = RoundedCornerShape(6.dp),
                                color = Color.Black.copy(alpha = 0.8f)
                            ) {
                                Text(
                                    text = badgeText.uppercase(),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFB74D)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // TITLE & BASE PRICE
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = item.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$${"%.2f".format(item.price)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.description,
                        fontSize = 12.sp,
                        color = Color(0xFF7D7A75),
                        lineHeight = 16.sp
                    )

                    // 2. KEY INGREDIENTS SECTION
                    if (!item.ingredients.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "KEY INGREDIENTS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8A8278)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item.ingredients.forEach { ingredient ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, lightBorder),
                                    color = Color.White
                                ) {
                                    Text(
                                        text = ingredient,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        fontSize = 11.sp,
                                        color = Color(0xFF5A524A)
                                    )
                                }
                            }
                        }
                    }

                    // 3. SIZE SELECTION SECTION
                    if (!item.sizes.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        SectionContainer(title = "Size Selection") {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                item.sizes.forEach { size ->
                                    val isSelected = selectedSize == size
                                    SelectableCard(
                                        modifier = Modifier.weight(1f),
                                        isSelected = isSelected,
                                        primaryColor = primaryColor,
                                        onClick = { selectedSize = size }
                                    ) {
                                        Text(
                                            text = size.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = if (isSelected) primaryColor else Color.Black
                                        )
                                        Text(
                                            text = "+$${"%.2f".format(size.priceModifier)}",
                                            fontSize = 11.sp,
                                            color = if (isSelected) primaryColor else Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 4. MILK OPTIONS / SPREADS SECTION
                    if (!item.milkOptions.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        SectionContainer(title = "Milk Options / Spreads") {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                item.milkOptions.forEach { milk ->
                                    val isSelected = selectedMilk == milk
                                    SelectableCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        isSelected = isSelected,
                                        primaryColor = primaryColor,
                                        onClick = { selectedMilk = milk }
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = milk.name,
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 12.sp,
                                                color = if (isSelected) primaryColor else Color.Black
                                            )
                                            Text(
                                                text = "+$${"%.2f".format(milk.priceModifier)}",
                                                fontSize = 12.sp,
                                                color = if (isSelected) primaryColor else Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // QUANTITY & ADD TO CART ROW
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, lightBorder),
                            color = Color.White
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text("-", Modifier.clickable { if (quantity > 1) quantity-- }, fontSize = 18.sp)
                                Text(" $quantity ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("+", Modifier.clickable { quantity++ }, fontSize = 18.sp)
                            }
                        }

                        Button(
                            onClick = {
                                viewModel.addToCart(
                                    CartItem(
                                        item = item,
                                        selectedSize = selectedSize,
                                        selectedMilk = selectedMilk,
                                        selectedSugar = selectedSugar,
                                        quantity = quantity
                                    )
                                )
                                onBack()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Add to Cart  •  $${"%.2f".format(unitPrice * quantity)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun SectionContainer(title: String, content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEFE8E0))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
fun SelectableCard(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    primaryColor: Color,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Color(0xFFFDF4F0) else Color.White)
            .border(
                width = 1.dp,
                color = if (isSelected) primaryColor else Color(0xFFEFE8E0),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}