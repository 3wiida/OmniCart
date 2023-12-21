package com.mahmoudibrahem.omnicart.core.navigation

object ScreenRoutes {
    const val LOGIN_SCREEN_ROUTE = "LOGIN_SCREEN_ROUTE"
    const val SIGNUP_SCREEN_ROUTE = "SIGNUP_SCREEN_ROUTE"
    const val HOME_SCREEN_ROUTE = "HOME_SCREEN_ROUTE"
    const val SEARCH_RESULTS_SCREEN_ROUTE = "SEARCH_RESULTS_SCREEN_ROUTE/{query}"
    const val SINGLE_PRODUCT_INFO_ROUTE = "SINGLE_PRODUCT_INFO_ROUTE/{product_id}"
    const val ALL_REVIEWS_SCREEN_ROUTE = "ALL_REVIEWS_SCREEN_ROUTE/{reviews}"
    const val EXPLORE_SCREEN_ROUTE = "EXPLORE_SCREEN_ROUTE/{categories}"
    const val CART_SCREEN_ROUTE = "CART_SCREEN_ROUTE"
    const val ALL_CATEGORIES_SCREEN_ROUTE = "ALL_CATEGORIES_SCREEN_ROUTE"
    const val USER_ADDRESS_SCREEN_ROUTE = "USER_ADDRESS_SCREEN_ROUTE/{is_from_cart}"
    const val ADD_ADDRESS_SCREEN_ROUTE = "ADD_ADDRESS_SCREEN_ROUTE"
    const val ACCOUNT_SCREEN_ROUTE = "ACCOUNT_SCREEN_ROUTE"
    const val FAVORITES_SCREEN_ROUTE = "FAVORITES_SCREEN_ROUTE"
    const val SUCCESS_SCREEN_ROUTE = "SUCCESS_SCREEN_ROUTE"
    const val ORDERS_SCREEN_ROUTE = "ORDERS_SCREEN_ROUTE"
    const val SINGLE_ORDER_SCREEN_ROUTE = "SINGLE_ORDER_SCREEN_ROUTE/{order_id}"
    const val PROFILE_SCREEN_ROUTE = "PROFILE_SCREEN_ROUTE"
    const val OFFER_SCREEN_ROUTE = "OFFER_SCREEN_ROUTE"
    const val CATEGORY_PRODUCTS_SCREEN_ROUTE = "CATEGORY_PRODUCTS_SCREEN_ROUTE/{category_name}"
    const val PRODUCTS_SCREEN_ROUTE = "PRODUCTS_SCREEN_ROUTE/{header}/{products}"
    const val EMAIL_ENTER_SCREEN_ROUTE = "EMAIL_ENTER_SCREEN_ROUTE"
    const val OTP_ENTER_SCREEN_ROUTE = "OTP_ENTER_SCREEN_ROUTE/{otp}"
    const val RESET_PASSWORD_SCREEN_ROUTE = "RESET_PASSWORD_SCREEN_ROUTE/{token}"
}