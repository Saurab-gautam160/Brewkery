package com.example.brewkery.userInterface.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brewkery.userInterface.BrewkeryViewModel

@Composable
fun CartScreen(
    viewModel: BrewkeryViewModel,
    onBack: () -> Unit,
    onOrderPlaced: (String) -> Unit
) {
    val cart by viewModel.cart.collectAsState()
    val warmBg = Color(0xFFFAF7F2)
    val primaryColor = Color(0xFFA03318)
    val lightBorder = Color(0xFFEFE8E0)

    val subtotal = cart.sumOf { it.totalPrice }
    val deliveryFee = if (cart.isNotEmpty()) 2.50 else 0.0
    val tax = subtotal * 0.08
    val total = subtotal + deliveryFee + tax

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(warmBg)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(24.dp))

            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.dp, lightBorder, CircleShape)
                        .background(Color.White)
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("←", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Text("YOUR CART", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                Text(
                    text = "Clear Cart",
                    fontSize = 12.sp,
                    color = primaryColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { viewModel.clearCart() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (cart.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Your cart is empty ☕", color = Color.Gray, fontSize = 16.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(cart) { cartItem ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White,
                            border = androidx.compose.foundation.BorderStroke(1.dp, lightBorder)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(14.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(cartItem.item.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    val sizeStr = cartItem.selectedSize?.name ?: ""
                                    val milkStr = cartItem.selectedMilk?.name ?: ""
                                    val subText = listOf(sizeStr, milkStr).filter { it.isNotEmpty() }.joinToString(" • ")
                                    if (subText.isNotEmpty()) {
                                        Text(subText, fontSize = 12.sp, color = Color.Gray)
                                    }
                                    Text(
                                        "$${"%.2f".format(cartItem.totalPrice)}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = primaryColor
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFF7F4F0)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text("-", Modifier.clickable { viewModel.updateQuantity(cartItem, -1) }, fontSize = 16.sp)
                                        Text("  ${cartItem.quantity}  ", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text("+", Modifier.clickable { viewModel.updateQuantity(cartItem, 1) }, fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                // Summary Calculation Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, lightBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text("Subtotal", color = Color.Gray, fontSize = 13.sp)
                            Text("$${"%.2f".format(subtotal)}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text("Delivery Fee", color = Color.Gray, fontSize = 13.sp)
                            Text("$${"%.2f".format(deliveryFee)}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text("Est. Tax (8.0%)", color = Color.Gray, fontSize = 13.sp)
                            Text("$${"%.2f".format(tax)}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Divider(color = lightBorder, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text("Total Payable", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("$${"%.2f".format(total)}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = primaryColor)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Place Order Button
                Button(
                    onClick = {
                        val ticketId = viewModel.placeOrder()
                        onOrderPlaced(ticketId)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("🔒  Place Order Now  •  $${"%.2f".format(total)}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }
}