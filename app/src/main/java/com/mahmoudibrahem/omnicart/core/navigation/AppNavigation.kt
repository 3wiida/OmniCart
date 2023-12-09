package com.mahmoudibrahem.omnicart.core.navigation

import android.app.Activity
import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mahmoudibrahem.omnicart.core.util.Converters.fromReviewsJson
import com.mahmoudibrahem.omnicart.presentation.screens.all_reviews.AllReviewsScreen
import com.mahmoudibrahem.omnicart.presentation.screens.auth.login.LoginScreen
import com.mahmoudibrahem.omnicart.presentation.screens.auth.register.RegisterScreen
import com.mahmoudibrahem.omnicart.presentation.screens.explore.ExploreScreen
import com.mahmoudibrahem.omnicart.presentation.screens.home.HomeScreen
import com.mahmoudibrahem.omnicart.presentation.screens.search_results.SearchResultsScreen
import com.mahmoudibrahem.omnicart.presentation.screens.single_product.SingleProductScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String,
    host: Activity
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(route = AppScreens.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(route = AppScreens.Register.route) },
                onNavigateToHome = {
                    navController.navigate(route = AppScreens.Home.route) {
                        this.popUpTo(AppScreens.Login.route) {
                            this.inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = AppScreens.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate(route = AppScreens.Login.route) },
                onNavigateToHome = { navController.navigate(route = AppScreens.Login.route) }
            )

        }

        composable(route = AppScreens.Home.route) {
            HomeScreen(
                onNavigateToSearchResults = { query ->
                    navController.navigate(
                        route = AppScreens.SearchResults.route.replace(
                            "{query}",
                            query
                        )
                    )
                },
                onNavigateToSingleProduct = { productID ->
                    navController.navigate(
                        route = AppScreens.SingleProductInfo.route.replace(
                            "{product_id}",
                            productID
                        )
                    )
                },
                onNavigateToExplore = {
                    navController.navigate(route = AppScreens.Explore.route)
                }
            )
        }

        composable(route = AppScreens.SearchResults.route) { navBackStackEntry ->
            val query = navBackStackEntry.arguments?.getCharSequence("query")
            if (query == null) {
                SearchResultsScreen()
            } else {
                SearchResultsScreen(
                    startingQuery = query.toString()
                )
            }
        }

        composable(route = AppScreens.SingleProductInfo.route) { navBackStackEntry ->
            val productID = navBackStackEntry.arguments?.getCharSequence("product_id")
            if (productID != null) {
                SingleProductScreen(
                    productID = productID.toString(),
                    onBackClicked = {
                        navController.popBackStack()
                    },
                    onSearchClicked = {
                        navController.navigate(route = AppScreens.SearchResults.route)
                    },
                    onNavigateToAllReviews = { reviewsJson ->
                        navController.navigate(
                            route = AppScreens.AllReviews.route.replace(
                                "{reviews}",
                                reviewsJson
                            )
                        )
                    }
                )
            }
        }

        composable(route = AppScreens.AllReviews.route) { navBackStackEntry ->
            val reviewsListJson = navBackStackEntry.arguments?.getString("reviews")
            if (reviewsListJson != null) {
                val reviewsList = reviewsListJson.fromReviewsJson()
                AllReviewsScreen(
                    reviews = reviewsList,
                    onBackPressed = { navController.popBackStack() }
                )
            }
        }

        composable(route = AppScreens.Explore.route) {
            ExploreScreen(
                onNavigateToHome = {
                    navController.navigate(route = AppScreens.Home.route)
                }
            )
        }
    }
}