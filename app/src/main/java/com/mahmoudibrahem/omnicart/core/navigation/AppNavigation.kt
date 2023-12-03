package com.mahmoudibrahem.omnicart.core.navigation

import android.app.Activity
import androidx.compose.runtime.Composable

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mahmoudibrahem.omnicart.presentation.screens.auth.login.LoginScreen
import com.mahmoudibrahem.omnicart.presentation.screens.auth.register.RegisterScreen
import com.mahmoudibrahem.omnicart.presentation.screens.home.HomeScreen
import com.mahmoudibrahem.omnicart.presentation.screens.search_results.SearchResultsScreen

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
    }
}