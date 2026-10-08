package com.example.brewkery.userInterface.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OrderStatusScreen(
    ticketId: String,
    onHomeClick: () -> Unit
) {
    val warmBg = Color(0xFFFAF7F2)
    val primaryColor = Color(0xFFA03318)
    val lightBorder = Color(0xFFEFE8E0)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(warmBg)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icon Badge
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFDF4F0))
                    .border(2.dp, primaryColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("☕", fontSize = 36.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("ORDER DISPATCHED", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Brewing in Progress!", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Your ticket was dispatched to our barista.", fontSize = 13.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            // Ticket Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, lightBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("ORDER TICKET", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                            Text(if (ticketId.startsWith("#")) ticketId else "#$ticketId", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFAF0E6)
                        ) {
                            Text(
                                "PREPARING",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB8860B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = lightBorder, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Text("Estimated Wait:", fontSize = 13.sp, color = Color.Gray)
                        Text("20 – 30 minutes", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = primaryColor)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Text("Items Ordered:", fontSize = 13.sp, color = Color.Gray)
                        Text("2 Items", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = lightBorder, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Status:", fontSize = 11.sp, color = Color.Gray)
                    Text("Barista accepted your order!", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E8449))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Back to Menu
            Button(
                onClick = onHomeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1715)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Back to Menu", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
            }
        }
    }
}