package com.example.catalogone.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Category : Screen("category/{categoryName}") {
        fun createRoute(categoryName: String) = "category/$categoryName"
    }
    object Search : Screen("search?query={query}") {
        fun createRoute(query: String) = "search?query=$query"
    }
    object ProductDetails : Screen("product/{productId}") {
        fun createRoute(productId: Int) = "product/$productId"
    }
    object Enquiry : Screen("enquiry/{productId}") {
        fun createRoute(productId: Int) = "enquiry/$productId"
    }
    object ThankYou : Screen("thankyou/{productTitle}/{customerName}") {
        fun createRoute(productTitle: String, customerName: String) =
            "thankyou/${java.net.URLEncoder.encode(productTitle, "UTF-8")}/${java.net.URLEncoder.encode(customerName, "UTF-8")}"
    }
}
