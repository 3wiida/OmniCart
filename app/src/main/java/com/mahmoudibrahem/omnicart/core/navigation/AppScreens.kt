package com.mahmoudibrahem.omnicart.core.navigation

import com.mahmoudibrahem.omnicart.core.navigation.ScreenRoutes.ADD_ADDRESS_SCREEN_ROUTE
import com.mahmoudibrahem.omnicart.core.navigation.ScreenRoutes.ALL_CATEGORIES_SCREEN_ROUTE
import com.mahmoudibrahem.omnicart.core.navigation.ScreenRoutes.ALL_REVIEWS_SCREEN_ROUTE
import com.mahmoudibrahem.omnicart.core.navigation.ScreenRoutes.CART_SCREEN_ROUTE
import com.mahmoudibrahem.omnicart.core.navigation.ScreenRoutes.EXPLORE_SCREEN_ROUTE
import com.mahmoudibrahem.omnicart.core.navigation.ScreenRoutes.HOME_SCREEN_ROUTE
import com.mahmoudibrahem.omnicart.core.navigation.ScreenRoutes.LOGIN_SCREEN_ROUTE
import com.mahmoudibrahem.omnicart.core.navigation.ScreenRoutes.SEARCH_RESULTS_SCREEN_ROUTE
import com.mahmoudibrahem.omnicart.core.navigation.ScreenRoutes.SIGNUP_SCREEN_ROUTE
import com.mahmoudibrahem.omnicart.core.navigation.ScreenRoutes.SINGLE_PRODUCT_INFO_ROUTE
import com.mahmoudibrahem.omnicart.core.navigation.ScreenRoutes.USER_ADDRESS_SCREEN_ROUTE

sealed class AppScreens(val route: String) {
    object Login : AppScreens(route = LOGIN_SCREEN_ROUTE)
    object Register : AppScreens(route = SIGNUP_SCREEN_ROUTE)
    object Home : AppScreens(route = HOME_SCREEN_ROUTE)
    object SearchResults : AppScreens(route = SEARCH_RESULTS_SCREEN_ROUTE)
    object SingleProductInfo : AppScreens(route = SINGLE_PRODUCT_INFO_ROUTE)
    object AllReviews : AppScreens(route = ALL_REVIEWS_SCREEN_ROUTE)
    object Explore : AppScreens(route = EXPLORE_SCREEN_ROUTE)
    object Cart : AppScreens(route = CART_SCREEN_ROUTE)
    object AllCategories : AppScreens(route = ALL_CATEGORIES_SCREEN_ROUTE)
    object UserAddress : AppScreens(route = USER_ADDRESS_SCREEN_ROUTE)
    object AddAddress : AppScreens(route = ADD_ADDRESS_SCREEN_ROUTE)
}
