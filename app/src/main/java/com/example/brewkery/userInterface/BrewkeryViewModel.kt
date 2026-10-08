package com.example.brewkery.userInterface



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewkery.data.model.*
import com.example.brewkery.data.model.remote.BrewkeryApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

class BrewkeryViewModel(
    private val apiService: BrewkeryApiService = BrewkeryApiService.create()
) : ViewModel() {

    private val _menuState = MutableStateFlow<UiState<MenuResponse>>(UiState.Loading)
    val menuState: StateFlow<UiState<MenuResponse>> = _menuState.asStateFlow()

    private val _detailState = MutableStateFlow<UiState<MenuItem>>(UiState.Loading)
    val detailState: StateFlow<UiState<MenuItem>> = _detailState.asStateFlow()

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _activeOrderId = MutableStateFlow<String?>(null)
    val activeOrderId: StateFlow<String?> = _activeOrderId.asStateFlow()

    init {
        loadMenu()
    }

    fun loadMenu() {
        viewModelScope.launch {
            _menuState.value = UiState.Loading
            try {
                val response = apiService.getFullMenu()
                _menuState.value = UiState.Success(response)
            } catch (e: Exception) {
                _menuState.value = UiState.Error(e.localizedMessage ?: "Failed to connect")
            }
        }
    }

    fun loadItemDetail(id: String) {
        viewModelScope.launch {
            _detailState.value = UiState.Loading

            // 1. Check if the item already exists in local menu state first
            val currentMenu = (_menuState.value as? UiState.Success)?.data?.items
            val existingItem = currentMenu?.find { it.id == id }

            if (existingItem != null) {
                _detailState.value = UiState.Success(existingItem)
                return@launch
            }

            // 2. Fetch remote item detail as fallback
            try {
                val item = apiService.getItemDetail(id)
                _detailState.value = UiState.Success(item)
            } catch (e: Exception) {
                _detailState.value = UiState.Error(e.localizedMessage ?: "Failed to load item")
            }
        }
    }




    fun setCategory(category: String) { _selectedCategory.value = category }
    fun setSearchQuery(query: String) { _searchQuery.value = query }

    fun addToCart(cartItem: CartItem) {
        _cart.update { current -> current + cartItem }
    }

    fun updateQuantity(cartItem: CartItem, delta: Int) {
        _cart.update { current ->
            current.mapNotNull { item ->
                if (item == cartItem) {
                    val newQty = item.quantity + delta
                    if (newQty > 0) item.copy(quantity = newQty) else null
                } else item
            }
        }
    }

    fun placeOrder(): String {
        val ticketId = "BK-${(1000..9999).random()}"
        _activeOrderId.value = ticketId
        _cart.value = emptyList()
        return ticketId
    }



    fun clearCart() {
        _cart.value = emptyList()
    }
}