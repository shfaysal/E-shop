package com.example.e_shop.ui.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_shop.data.model.Product
import com.example.e_shop.data.repository.ProductRepository
import com.example.e_shop.util.SafeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val relatedProducts: List<Product> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private val productId: Int? = savedStateHandle.get<Int>("productId")

    init {
        productId?.let { id ->
            fetchProductDetails(id)
        }
    }

    private fun fetchProductDetails(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val productResult = productRepository.getProduct(id)
            
            // If product fetch succeeds, try to fetch related products
            // Note: The API for related products might fail if the endpoint doesn't strictly exist 
            // exactly as we assumed, but we'll try it. 
            // If it fails or returns empty, we might fallback to category based on the fetched product.
            
            if (productResult is SafeResult.Success) {
                val product = productResult.data
                
                // Parallel or sequential fetch for related? 
                // Let's do sequential for simplicity to use the category ID if needed for fallback
                
                // Try the specific related endpoint first
                var relatedResult = productRepository.getRelatedProducts(id)
                
                // Fallback mechanism: if related API fails or empty, fetch by category
                if (relatedResult is SafeResult.Error || (relatedResult is SafeResult.Success && relatedResult.data.isEmpty())) {
                     product.category?.id?.let { catId ->
                         val categoryResult = productRepository.getProductsByCategory(catId)
                         if (categoryResult is SafeResult.Success) {
                             // Filter out the current product
                             val related = categoryResult.data.filter { it.id != id }
                             relatedResult = SafeResult.Success(related)
                         }
                     }
                }

                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        product = product,
                        relatedProducts = if (relatedResult is SafeResult.Success) relatedResult.data else emptyList(),
                        error = null
                    )
                }
            } else {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = (productResult as SafeResult.Error).message
                    )
                }
            }
        }
    }
}