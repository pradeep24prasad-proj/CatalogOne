package com.example.catalogone.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.catalogone.presentation.category.CategoryScreen
import com.example.catalogone.presentation.category.CategoryViewModel
import com.example.catalogone.presentation.enquiry.EnquiryScreen
import com.example.catalogone.presentation.enquiry.EnquiryViewModel
import com.example.catalogone.presentation.enquiry.ThankYouScreen
import com.example.catalogone.presentation.home.HomeScreen
import com.example.catalogone.presentation.home.HomeViewModel
import com.example.catalogone.presentation.productdetails.ProductDetailsScreen
import com.example.catalogone.presentation.productdetails.ProductDetailsViewModel
import com.example.catalogone.presentation.search.SearchScreen
import com.example.catalogone.presentation.search.SearchViewModel
import java.net.URLDecoder

@Composable
fun CatalogNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // 1. Home Screen
        composable(Screen.Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                viewModel = viewModel,
                onCategorySelect = { category ->
                    navController.navigate(Screen.Category.createRoute(category))
                },
                onSearchSubmit = { query ->
                    navController.navigate(Screen.Search.createRoute(query))
                },
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetails.createRoute(productId))
                }
            )
        }

        // 2. Category Screen
        composable(
            route = Screen.Category.route,
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val viewModel: CategoryViewModel = hiltViewModel()
            CategoryScreen(
                categoryName = categoryName,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetails.createRoute(productId))
                }
            )
        }

        // 3. Search Screen
        composable(
            route = Screen.Search.route,
            arguments = listOf(navArgument("query") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            val viewModel: SearchViewModel = hiltViewModel()
            SearchScreen(
                initialQuery = query,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetails.createRoute(productId))
                }
            )
        }

        // 4. Product Details Screen
        composable(
            route = Screen.ProductDetails.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            val viewModel: ProductDetailsViewModel = hiltViewModel()
            ProductDetailsScreen(
                productId = productId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onEnquireClick = { id ->
                    navController.navigate(Screen.Enquiry.createRoute(id))
                }
            )
        }

        // 5. Enquiry Screen
        composable(
            route = Screen.Enquiry.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            val viewModel: EnquiryViewModel = hiltViewModel()
            EnquiryScreen(
                productId = productId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onSubmitSuccess = { productTitle, customerName ->
                    navController.navigate(Screen.ThankYou.createRoute(productTitle, customerName)) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                }
            )
        }

        // 6. Thank You Screen
        composable(
            route = Screen.ThankYou.route,
            arguments = listOf(
                navArgument("productTitle") { type = NavType.StringType },
                navArgument("customerName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val rawTitle = backStackEntry.arguments?.getString("productTitle") ?: ""
            val rawName = backStackEntry.arguments?.getString("customerName") ?: ""
            val productTitle = URLDecoder.decode(rawTitle, "UTF-8")
            val customerName = URLDecoder.decode(rawName, "UTF-8")

            ThankYouScreen(
                productTitle = productTitle,
                customerName = customerName,
                onHomeClick = {
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                }
            )
        }
    }
}
