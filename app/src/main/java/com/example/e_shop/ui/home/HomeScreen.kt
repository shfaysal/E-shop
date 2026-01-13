package com.example.e_shop.ui.home

import androidx.compose.ui.res.dimensionResource
import com.example.e_shop.R

import androidx.compose.ui.res.stringResource

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_large))
    ) {
        // Search Bar
        SearchBar(
            query = uiState.searchQuery,
            onQueryChanged = viewModel::onSearchQueryChanged
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Categories
            AppText(
                text = stringResource(R.string.categories),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small))
            )
            CategoryList(
                categories = uiState.categories,
                selectedCategory = uiState.selectedCategory,
                onCategorySelected = viewModel::onCategorySelected
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

            // Products
            AppText(
                text = if (uiState.selectedCategory != null) 
                    stringResource(R.string.category_products, uiState.selectedCategory?.safeName ?: "") 
                else 
                    stringResource(R.string.new_arrivals),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_small))
            )
            ProductGrid(products = uiState.filteredProducts, navController = navController)
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        placeholder = { AppText(stringResource(R.string.search_products)) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search_products)) },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(dimensionResource(R.dimen.corner_radius_medium))),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun CategoryList(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        items(categories) { category ->
            CategoryItem(
                category = category,
                isSelected = category.id == selectedCategory?.id,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(dimensionResource(R.dimen.category_item_width))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = category.safeImage,
            contentDescription = category.safeName,
            modifier = Modifier
                .size(dimensionResource(R.dimen.category_image_size))
                .clip(CircleShape)
                .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.LightGray),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_tiny)))
        AppText(
            text = category.safeName,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified,
            fontWeight = if (isSelected) FontWeight.Bold else null
        )
    }
}

@Composable
fun ProductGrid(products: List<Product>, navController: NavController) {
    if (products.isEmpty()) {
        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
            AppText(stringResource(R.string.no_products_found))
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            modifier = Modifier.fillMaxSize()
        ) {
            items(products) { product ->
                ProductItem(
                    product = product,
                    onClick = {
                        navController.navigate(Screen.ProductDetail.createRoute(product.id ?: 0))
                    }
                )
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(dimensionResource(R.dimen.corner_radius_large)),
        elevation = CardDefaults.cardElevation(defaultElevation = dimensionResource(R.dimen.card_elevation)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column {
            AsyncImage(
                model = product.firstImage,
                contentDescription = product.safeTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.product_item_height))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))) {
                AppText(
                    text = product.safeTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_tiny)))
                AppText(
                    text = "$${product.safePrice}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}