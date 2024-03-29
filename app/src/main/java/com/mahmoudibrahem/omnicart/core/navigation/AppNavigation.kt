package com.mahmoudibrahem.omnicart.core.navigation

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mahmoudibrahem.omnicart.core.util.Converters.toProductsList
import com.mahmoudibrahem.omnicart.core.util.Converters.toReviewList
import com.mahmoudibrahem.omnicart.presentation.screens.account.AccountScreen
import com.mahmoudibrahem.omnicart.presentation.screens.add_address.AddAddressScreen
import com.mahmoudibrahem.omnicart.presentation.screens.all_categories.AllCategoriesScreen
import com.mahmoudibrahem.omnicart.presentation.screens.all_reviews.AllReviewsScreen
import com.mahmoudibrahem.omnicart.presentation.screens.auth.forget_password.EmailEnterScreen
import com.mahmoudibrahem.omnicart.presentation.screens.auth.forget_password.OTPEnterScreen
import com.mahmoudibrahem.omnicart.presentation.screens.auth.forget_password.ResetPasswordScreen
import com.mahmoudibrahem.omnicart.presentation.screens.auth.login.LoginScreen
import com.mahmoudibrahem.omnicart.presentation.screens.auth.register.RegisterScreen
import com.mahmoudibrahem.omnicart.presentation.screens.cart.CartScreen
import com.mahmoudibrahem.omnicart.presentation.screens.category_products.CategoryProductsScreen
import com.mahmoudibrahem.omnicart.presentation.screens.explore.ExploreScreen
import com.mahmoudibrahem.omnicart.presentation.screens.favorites.FavoritesScreen
import com.mahmoudibrahem.omnicart.presentation.screens.home.HomeScreen
import com.mahmoudibrahem.omnicart.presentation.screens.offer.OfferScreen
import com.mahmoudibrahem.omnicart.presentation.screens.orders.OrdersScreen
import com.mahmoudibrahem.omnicart.presentation.screens.products.ProductsScreen
import com.mahmoudibrahem.omnicart.presentation.screens.profile.ProfileScreen
import com.mahmoudibrahem.omnicart.presentation.screens.search_results.SearchResultsScreen
import com.mahmoudibrahem.omnicart.presentation.screens.single_order.SingleOrderScreen
import com.mahmoudibrahem.omnicart.presentation.screens.single_product.SingleProductScreen
import com.mahmoudibrahem.omnicart.presentation.screens.success_screen.SuccessScreen
import com.mahmoudibrahem.omnicart.presentation.screens.user_address.UserAddressScreen
import com.mahmoudibrahem.omnicart.presentation.screens.write_review.WriteReviewScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            fadeIn(animationSpec = SpringSpec(dampingRatio = 0.6573f, stiffness = 100f))
        },
        exitTransition = {
            fadeOut(animationSpec = SpringSpec(dampingRatio = 0.6573f, stiffness = 100f))
        }
    ) {
        /**Auth Screens*/
        composable(route = AppScreens.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(route = AppScreens.Register.route) },
                onNavigateToHome = {
                    navController.navigate(route = AppScreens.Home.route) {
                        this.popUpTo(AppScreens.Login.route) {
                            this.inclusive = true
                        }
                    }
                },
                onNavigateToForgotPassword = {
                    navController.navigate(route = AppScreens.EmailEnter.route) {
                    }
                }
            )
        }


        composable(
            route = AppScreens.Register.route,
        ) {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate(route = AppScreens.Login.route) },
                onNavigateToHome = { navController.navigate(route = AppScreens.Login.route) }
            )

        }

        composable(route = AppScreens.EmailEnter.route) {
            EmailEnterScreen(
                onNavigateToOTPEnter = { correctOTP ->
                    navController.navigate(
                        route = AppScreens.OTPEnter.route.replace(
                            "{otp}",
                            correctOTP
                        )
                    )
                }
            )
        }

        composable(route = AppScreens.OTPEnter.route) { navBackStackEntry ->
            val correctOTP = navBackStackEntry.arguments?.getString("otp")
            correctOTP?.let {
                OTPEnterScreen(
                    correctOTP = it,
                    onNavigateToResetPassword = { token ->
                        navController.navigate(
                            route = AppScreens.ResetPassword.route.replace(
                                "{token}",
                                token
                            )
                        ) {
                            popUpTo(route = AppScreens.Login.route)
                        }
                    }
                )
            }
        }

        composable(route = AppScreens.ResetPassword.route) { navBackStackEntry ->
            val token = navBackStackEntry.arguments?.getString("token")
            token?.let {
                ResetPasswordScreen(
                    token = it,
                    onNavigateToLogin = {
                        navController.navigate(route = AppScreens.Login.route) {
                            popUpTo(AppScreens.Login.route)
                        }
                    }
                )
            }
        }


        /**Bottom Bar Screens*/
        composable(route = AppScreens.Home.route) {
            HomeScreen(
                onNavigateToExplore = {
                    navController.navigate(route = AppScreens.Explore.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                },
                onNavigateToCart = {
                    navController.navigate(route = AppScreens.Cart.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                },
                onNavigateToOffer = {
                    navController.navigate(route = AppScreens.Offer.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                },
                onNavigateToAccount = {
                    navController.navigate(route = AppScreens.Account.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                },
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
                onNavigateToAllCategories = {
                    navController.navigate(route = AppScreens.AllCategories.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                },
                onNavigateToFavorites = {
                    navController.navigate(route = AppScreens.Favorites.route)
                },
                onNavigateToSingleCategory = { categoryName ->
                    navController.navigate(
                        route = AppScreens.CategoryProducts.route.replace(
                            "{category_name}",
                            categoryName
                        )
                    )
                },
                onNavigateToProducts = { header, productJson ->
                    navController.navigate(
                        route = AppScreens.ProductsScreen.route
                            .replace(
                                "{products}",
                                productJson
                            ).replace(
                                "{header}",
                                header
                            )
                    )
                }
            )
        }

        composable(route = AppScreens.Explore.route) {
            ExploreScreen(
                onNavigateToHome = {
                    /*navController.navigate(route = AppScreens.Home.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }*/
                    navController.navigateUp()
                },
                onNavigateToCart = {
                    navController.navigate(route = AppScreens.Cart.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                },
                onNavigateToOffer = {
                    navController.navigate(route = AppScreens.Offer.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                },
                onNavigateToAccount = {
                    navController.navigate(route = AppScreens.Account.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                },
                onNavigateToFav = {
                    navController.navigate(route = AppScreens.Favorites.route)
                },
                onNavigateToSearchResults = { query ->
                    navController.navigate(
                        route = AppScreens.SearchResults.route.replace(
                            "{query}",
                            query
                        )
                    )
                },
                onNavigateToSingleCategory = { categoryName ->
                    navController.navigate(
                        route = AppScreens.CategoryProducts.route.replace(
                            "{category_name}",
                            categoryName
                        )
                    )
                },
            )
        }

        composable(route = AppScreens.Cart.route) {
            CartScreen(
                onNavigateToHome = {
                    /*navController.navigate(route = AppScreens.Home.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }*/
                    navController.navigateUp()
                },
                onNavigateToExplore = {
                    navController.navigate(route = AppScreens.Explore.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                },
                onNavigateToOffer = {
                    navController.navigate(route = AppScreens.Offer.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                },
                onNavigateToAccount = {
                    navController.navigate(route = AppScreens.Account.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                },
                onNavigateToAddress = {
                    navController.navigate(
                        route = AppScreens.UserAddress.route.replace(
                            "{is_from_cart}",
                            "true"
                        )
                    )
                }
            )
        }

        composable(route = AppScreens.Offer.route) {
            OfferScreen(
                onNavigateToSingleProduct = { productID ->
                    navController.navigate(
                        route = AppScreens.SingleProductInfo.route.replace(
                            "{product_id}",
                            productID
                        )
                    )
                },
                onNavigateToHome = {
                    /*navController.navigate(route = AppScreens.Home.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }*/
                    navController.navigateUp()
                },
                onNavigateToExplore = {
                    navController.navigate(route = AppScreens.Explore.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                },
                onNavigateToCart = {
                    navController.navigate(route = AppScreens.Cart.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                },
                onNavigateToAccount = {
                    navController.navigate(route = AppScreens.Account.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                },
            )
        }

        composable(route = AppScreens.Account.route) {
            AccountScreen(
                onNavigateToHome = {
                    /*navController.navigate(route = AppScreens.Home.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }*/
                    navController.navigateUp()
                },
                onNavigateToExplore = {
                    navController.navigate(route = AppScreens.Explore.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                },
                onNavigateToCart = {
                    navController.navigate(route = AppScreens.Cart.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                },
                onNavigateToOffer = {
                    navController.navigate(route = AppScreens.Offer.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                },
                onNavigateToAccountOption = { option ->
                    when (option) {
                        0 -> {
                            navController.navigate(route = AppScreens.Profile.route)
                        }

                        1 -> {
                            navController.navigate(route = AppScreens.Orders.route)
                        }

                        2 -> {
                            navController.navigate(
                                route = AppScreens.UserAddress.route.replace(
                                    "{is_from_cart}",
                                    "false"
                                )
                            )
                        }
                    }
                }
            )
        }


        /**Single Product Screens*/
        composable(route = AppScreens.SingleProductInfo.route) { navBackStackEntry ->
            val productID = navBackStackEntry.arguments?.getCharSequence("product_id")
            if (productID != null) {
                SingleProductScreen(
                    productID = productID.toString(),
                    onBackClicked = {
                        navController.popBackStack()
                    },
                    onNavigateToAllReviews = { reviewsJson ->
                        navController.navigate(
                            route = AppScreens.AllReviews.route
                                .replace(
                                    "{reviews}",
                                    reviewsJson
                                )
                                .replace(
                                    "{productID}",
                                    productID.toString()
                                )
                        )
                    },
                    onNavigateToWriteReview = {
                        navController.navigate(
                            route =
                            AppScreens.WriteReview.route.replace(
                                "{productID}",
                                productID.toString()
                            )
                        )
                    }
                )
            }
        }
        composable(route = AppScreens.AllReviews.route) { navBackStackEntry ->
            val reviewsListJson = navBackStackEntry.arguments?.getString("reviews")
            val productID = navBackStackEntry.arguments?.getString("productID")
            if (reviewsListJson != null && productID != null) {
                val reviewsList = reviewsListJson.toReviewList()
                AllReviewsScreen(
                    reviews = reviewsList,
                    productID = productID,
                    onBackPressed = { navController.popBackStack() },
                    onNavigateToWriteReview = {
                        navController.navigate(
                            route =
                            AppScreens.WriteReview.route.replace("{productID}", productID)
                        )
                    }
                )
            }
        }

        composable(route = AppScreens.WriteReview.route) { navBackStackEntry ->
            val productID = navBackStackEntry.arguments?.getString("productID")
            if (productID != null) {
                WriteReviewScreen(
                    productID = productID,
                    onNavigateUp = { navController.navigateUp() },
                    onNavigateToProduct = {
                        navController.navigate(
                            route = AppScreens.SingleProductInfo.route.replace(
                                "{product_id}",
                                productID
                            )
                        ) {
                            this.popUpTo(route = AppScreens.SingleProductInfo.route)
                        }
                    },
                )
            }
        }


        /**User Address Screens*/
        composable(route = AppScreens.UserAddress.route) { navBackStackEntry ->
            val isFromCart = navBackStackEntry.arguments?.getString("is_from_cart").toBoolean()
            UserAddressScreen(
                isFromCart = isFromCart,
                onBackClicked = { navController.navigateUp() },
                onNavigateToAddAddress = { navController.navigate(route = AppScreens.AddAddress.route) },
                onNavigateToSuccessScreen = {
                    navController.navigate(route = AppScreens.Success.route) {
                        popUpTo(route = AppScreens.Home.route)
                    }
                }
            )
        }

        composable(route = AppScreens.AddAddress.route) {
            AddAddressScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }


        /**Order Screens*/
        composable(route = AppScreens.Success.route) {
            SuccessScreen(
                onNavigateToOrders = {
                    navController.navigate(route = AppScreens.Orders.route) {
                        popUpTo(AppScreens.Home.route)
                    }
                }
            )
        }

        composable(route = AppScreens.Orders.route) {
            OrdersScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToHome = { navController.navigate(route = AppScreens.Home.route) },
                onNavigateToSingleOrder = { orderID ->
                    navController.navigate(
                        route = AppScreens.SingleOrder.route.replace(
                            "{order_id}",
                            orderID
                        )
                    )
                }
            )
        }

        composable(route = AppScreens.SingleOrder.route) { navBackStackEntry ->
            val orderID = navBackStackEntry.arguments?.getString("order_id")
            orderID?.let {
                SingleOrderScreen(
                    onBackClicked = { navController.navigateUp() },
                    onNavigateToSingleProduct = { productID ->
                        navController.navigate(
                            route = AppScreens.SingleProductInfo.route.replace(
                                "{product_id}",
                                productID
                            )
                        )
                    },
                    orderID = it
                )
            }
        }


        /**Other Screens*/
        composable(route = AppScreens.SearchResults.route) { navBackStackEntry ->
            val query = navBackStackEntry.arguments?.getCharSequence("query")
            if (query == null) {
                SearchResultsScreen(
                    onNavigateToSingleProduct = { productID ->
                        navController.navigate(
                            route = AppScreens.SingleProductInfo.route.replace(
                                "{product_id}",
                                productID
                            )
                        )
                    }
                )
            } else {
                SearchResultsScreen(
                    startingQuery = query.toString(),
                    onNavigateToSingleProduct = { productID ->
                        navController.navigate(
                            route = AppScreens.SingleProductInfo.route.replace(
                                "{product_id}",
                                productID
                            )
                        )
                    }
                )
            }
        }

        composable(route = AppScreens.AllCategories.route) {
            AllCategoriesScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                onNavigateToCategory = { categoryName ->
                    navController.navigate(
                        route = AppScreens.CategoryProducts.route.replace(
                            "{category_name}",
                            categoryName
                        )
                    )
                }
            )
        }

        composable(route = AppScreens.Favorites.route) {
            FavoritesScreen(
                onNavigationBack = {
                    navController.navigateUp()
                },
                onNavigateToProduct = { productID ->
                    navController.navigate(
                        route = AppScreens.SingleProductInfo.route.replace(
                            "{product_id}",
                            productID
                        )
                    )
                }
            )
        }

        composable(route = AppScreens.Profile.route) {
            ProfileScreen(
                onNavigateUp = { navController.navigateUp() },
                onNavigateToLogin = {
                    navController.navigate(route = AppScreens.Login.route) {
                        popUpTo(route = AppScreens.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = AppScreens.CategoryProducts.route) { navBackStackEntry ->
            val categoryName = navBackStackEntry.arguments?.getString("category_name")
            categoryName?.let { name ->
                CategoryProductsScreen(
                    categoryName = name,
                    onNavigateUp = { navController.navigateUp() },
                    onNavigateToSingleProduct = { productID ->
                        navController.navigate(
                            route = AppScreens.SingleProductInfo.route.replace(
                                "{product_id}",
                                productID
                            )
                        )
                    }
                )
            }
        }

        composable(route = AppScreens.ProductsScreen.route) { navBackStackEntry ->
            val header = navBackStackEntry.arguments?.getString("header")
            val productsJson = navBackStackEntry.arguments?.getString("products")
            if (header != null && productsJson != null) {
                val productsList = productsJson.toProductsList()
                ProductsScreen(
                    header = header,
                    products = productsList,
                    onNavigateUp = {
                        navController.navigateUp()
                    },
                    onNavigateToSingleProduct = { productID ->
                        navController.navigate(
                            route = AppScreens.SingleProductInfo.route.replace(
                                "{product_id}",
                                productID
                            )
                        )
                    }
                )
            }
        }

    }
}