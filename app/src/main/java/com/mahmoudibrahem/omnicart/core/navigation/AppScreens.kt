package com.mahmoudibrahem.omnicart.core.navigation

import com.mahmoudibrahem.omnicart.core.navigation.ScreenRoutes.HOME_SCREEN_ROUTE
import com.mahmoudibrahem.omnicart.core.navigation.ScreenRoutes.LOGIN_SCREEN_ROUTE
import com.mahmoudibrahem.omnicart.core.navigation.ScreenRoutes.SEARCH_RESULTS_SCREEN_ROUTE
import com.mahmoudibrahem.omnicart.core.navigation.ScreenRoutes.SIGNUP_SCREEN_ROUTE
import com.mahmoudibrahem.omnicart.core.navigation.ScreenRoutes.SINGLE_PRODUCT_INFO_ROUTE

sealed class AppScreens(val route: String) {
    object Login : AppScreens(route = LOGIN_SCREEN_ROUTE)
    object Register : AppScreens(route = SIGNUP_SCREEN_ROUTE)
    object Home : AppScreens(route = HOME_SCREEN_ROUTE)
    object SearchResults : AppScreens(route = SEARCH_RESULTS_SCREEN_ROUTE)
    object SingleProductInfo : AppScreens(route = SINGLE_PRODUCT_INFO_ROUTE)
}
