package com.example.e_shop.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.e_shop.R
import com.example.e_shop.data.model.Product
import com.example.e_shop.ui.components.AppText
import com.example.e_shop.ui.navigation.Screen

import androidx.compose.ui.res.stringResource

@Composable
fun ProductDetailScreen(
    navController: NavController,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            if (uiState.product != null) {
                Button(
                    onClick = { /* Add to Cart */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_large)),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_large))
                ) {
                    AppText(stringResource(R.string.add_to_cart), modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                AppText(
                    text = uiState.error ?: "Unknown error",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                uiState.product?.let { product ->
                    ProductContent(
                        product = product,
                        relatedProducts = uiState.relatedProducts,
                        navController = navController
                    )
                }
            }
            
            // Back Button
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_large))
                    .align(Alignment.TopStart)
                    .background(Color.White.copy(alpha = 0.7f), CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
            }
        }
    }
}

@Composable
fun ProductContent(
    product: Product,
    relatedProducts: List<Product>,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Product Image
        AsyncImage(
            model = product.firstImage,
            contentDescription = product.safeTitle,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.product_image_height_large))
                .background(Color.LightGray),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_large))) {
            // Category Tag
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium))
            ) {
                AppText(
                    text = product.safeCategory.safeName,
                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small), vertical = dimensionResource(R.dimen.padding_tiny)),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

            // Title
            AppText(
                text = product.safeTitle,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))

            // Price
            AppText(
                text = "$${product.safePrice}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

            // Description
            AppText(
                text = stringResource(R.string.description),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_small)))
            AppText(
                text = product.safeDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_xxl)))

            // Related Products
            if (relatedProducts.isNotEmpty()) {
                AppText(
                    text = stringResource(R.string.related_products),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                    contentPadding = PaddingValues(bottom = dimensionResource(R.dimen.padding_large))
                ) {
                    items(relatedProducts) { related ->
                        RelatedProductItem(
                            product = related,
                            onClick = {
                                navController.navigate(Screen.ProductDetail.createRoute(related.id ?: 0))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RelatedProductItem(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(dimensionResource(R.dimen.related_product_width))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_large)),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation))
    ) {
        Column {
            AsyncImage(
                model = product.firstImage,
                contentDescription = product.safeTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.related_product_image_height))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))) {
                AppText(
                    text = product.safeTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_tiny)))
                AppText(
                    text = "$${product.safePrice}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}