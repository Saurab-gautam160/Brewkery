package com.example.brewkery.userInterface.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.brewkery.data.model.MenuItem
import com.example.brewkery.userInterface.BrewkeryViewModel
import com.example.brewkery.userInterface.UiState
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    viewModel: BrewkeryViewModel,
    onItemClick: (String) -> Unit,
    onCartClick: () -> Unit
) {
    val menuState by viewModel.menuState.collectAsState()
    val selectedCat by viewModel.selectedCategory.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val cart by viewModel.cart.collectAsState()
    val activeOrderId by viewModel.activeOrderId.collectAsState()

    val primaryOrange = Color(0xFFD35400)
    val cardBg = Color(0xFFFFF8F5)

    Scaffold(
        bottomBar = {
            if (cart.isNotEmpty()) {
                val totalItems = cart.sumOf { it.quantity }
                val subtotal = cart.sumOf { it.totalPrice }
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { onCartClick() },
                    shape = RoundedCornerShape(24.dp),
                    color = Color.Black
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(primaryOrange, RoundedCornerShape(18.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("$totalItems", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("View Your Cart", color = Color.Gray, fontSize = 12.sp)
                                Text("$${"%.2f".format(subtotal)}", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text("Proceed to Checkout →", color = primaryOrange, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFBF8F5))
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Active Order Banner
            activeOrderId?.let { orderId ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F8F5))
                ) {
                    Text("Order $orderId is PREPARING ☕", modifier = Modifier.padding(12.dp), color = Color(0xFF117864), fontWeight = FontWeight.Bold)
                }
            }

            // Store Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🛵", fontSize = 24.sp)
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("STORE INFO •", color = primaryOrange, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text("Delivery in 20 – 30 mins", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("$2.50 flat fee", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                    Surface(shape = RoundedCornerShape(12.dp), color = Color.White) {
                        Text("Open", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search roast, cold brew, pastry...", fontSize = 13.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color(0xFFD35400)
                )
            )

            Spacer(Modifier.height(12.dp))

            // Categories
            // Get category names from the API response (or default to fallback list)
            val categoryList = remember(menuState) {
                if (menuState is UiState.Success) {
                    listOf("All") + ((menuState as UiState.Success).data.categories?.map { it.name } ?: emptyList())
                } else {
                    listOf("All", "Hot Coffee", "Cold Brews", "Artisan Bakery")
                }
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categoryList) { categoryName ->
                    val isSelected = categoryName == selectedCat
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setCategory(categoryName) },
                        label = { Text(categoryName) },
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Items List
            when (val state = menuState) {
                is UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = primaryOrange) }
                is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(state.message, color = Color.Red)
                        Button(onClick = { viewModel.loadMenu() }) { Text("Retry") }
                    }
                }
                is UiState.Success -> {
                    val filteredItems = state.data.items.filter {
                        (selectedCat == "All" || it.category.equals(selectedCat, ignoreCase = true)) &&
                                it.name.contains(query, ignoreCase = true)
                    }

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(filteredItems) { item ->
                            MenuItemCard(item = item, onClick = { onItemClick(item.id) })
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun MenuItemCard(item: MenuItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Fixed Image Sizing: Replaced .fillMaxWidth().height(180.dp) with fixed square size
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                item.badge?.let {
                    Text(
                        text = it.uppercase(),
                        color = Color(0xFFD35400),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 2
                )
                Text(
                    text = "★ ${item.rating} (${item.ratingCount})",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "$${"%.2f".format(item.price)}",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD35400)
                )
            }
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE55B13)),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text("+ Customize", fontSize = 12.sp)
            }
        }

    }


    @Composable
    fun MainTopBar(
        cartItemCount: Int,
        onCartClick: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Circle Logo Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "BK",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Brand Titles
                Column {
                    Text(
                        text = "Fresh Roast & Bakes",
                        fontSize = 11.sp,
                        color = Color(0xFF8A8278)
                    )
                    Text(
                        text = "Brewkery Artisans",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            // Cart Icon with Badge
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF7F3EE))
                    .clickable { onCartClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Cart",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )


                if (cartItemCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 2.dp, y = (-2).dp)
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFC04B22)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cartItemCount.toString(),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


