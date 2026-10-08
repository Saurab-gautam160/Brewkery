package com.example.brewkery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.brewkery.userInterface.BrewkeryViewModel
import com.example.brewkery.userInterface.screen.CartScreen
import com.example.brewkery.userInterface.screen.ItemDetailScreen
import com.example.brewkery.userInterface.screen.MenuScreen
import com.example.brewkery.userInterface.screen.OrderStatusScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: BrewkeryViewModel = viewModel()

            NavHost(navController = navController, startDestination = "menu") {
                composable("menu") {
                    MenuScreen(
                        viewModel = viewModel,
                        onItemClick = { itemId -> navController.navigate("detail/$itemId") },
                        onCartClick = { navController.navigate("cart") }
                    )
                }
                composable("detail/{itemId}") { backStackEntry ->
                    val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
                    ItemDetailScreen(
                        itemId = itemId,
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("cart") {
                    CartScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onOrderPlaced = { ticketId ->
                            navController.navigate("status/$ticketId") {
                                popUpTo("menu") { inclusive = false }
                            }
                        }
                    )
                }
                composable("status/{ticketId}") { backStackEntry ->
                    val ticketId = backStackEntry.arguments?.getString("ticketId") ?: ""
                    OrderStatusScreen(
                        ticketId = ticketId,
                        onHomeClick = { navController.navigate("menu") }
                    )
                }
            }
        }
    }
}