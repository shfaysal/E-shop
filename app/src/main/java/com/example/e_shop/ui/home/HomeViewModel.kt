package com.example.e_shop.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_shop.data.model.Category
import com.example.e_shop.data.model.Product
import com.example.e_shop.data.repository.ProductRepository
import com.example.e_shop.util.SafeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val filteredProducts: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val searchQuery: String = "",
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var allProducts: List<Product> = emptyList()
    private var searchJob: Job? = null

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val productsResult = productRepository.getProducts()
            val categoriesResult = productRepository.getCategories()

            _uiState.update { currentState ->
                val products = if (productsResult is SafeResult.Success) productsResult.data else emptyList()
                val categories = if (categoriesResult is SafeResult.Success) categoriesResult.data else emptyList()
                val error = if (productsResult is SafeResult.Error) productsResult.message 
                           else if (categoriesResult is SafeResult.Error) categoriesResult.message 
                           else null
                
                allProducts = products
                
                currentState.copy(
                    isLoading = false,
                    products = products,
                    filteredProducts = products,
                    categories = categories,
                    error = error
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // Debounce
            filterProducts()
        }
    }

    fun onCategorySelected(category: Category) {
        viewModelScope.launch {
            val isSameCategory = _uiState.value.selectedCategory?.id == category.id
            val newCategory = if (isSameCategory) null else category
            
            _uiState.update { it.copy(selectedCategory = newCategory, isLoading = true) }

            if (newCategory == null) {
                // Reset to all products
                // We could re-fetch or just use the cached allProducts if we trust it hasn't changed much
                // For better UX, let's re-use allProducts but maybe re-fetch if needed.
                // Assuming allProducts contains everything for now.
                 // Actually, if we fetched by category previously, allProducts might be stale or we might want to reset.
                 // Ideally, we keep "allProducts" as the master list if possible, or we fetch "all" again.
                 // Let's re-fetch all to be safe and simple, or just use the cached one.
                 // Let's use the cached one to avoid network call if we can.
                 // Wait, if we used getProductsByCategory, we replaced the list.
                 // So we should probably keep a separate cache or re-fetch.
                 // Let's re-fetch for simplicity to ensure freshness.
                val result = productRepository.getProducts()
                 if (result is SafeResult.Success) {
                     allProducts = result.data
                     filterProducts()
                 }
            } else {
                // Fetch by category
                val result = productRepository.getProductsByCategory(category.id ?: -1)
                if (result is SafeResult.Success) {
                    allProducts = result.data
                    filterProducts()
                }
            }
             _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun filterProducts() {
        val query = _uiState.value.searchQuery
        val filtered = if (query.isBlank()) {
            allProducts
        } else {
            allProducts.filter { 
                it.safeTitle.contains(query, ignoreCase = true) || 
                it.safeDescription.contains(query, ignoreCase = true) 
            }
        }
        _uiState.update { it.copy(filteredProducts = filtered) }
    }
}