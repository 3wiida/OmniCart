package com.mahmoudibrahem.omnicart.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.mahmoudibrahem.omnicart.core.navigation.AppNavigation
import com.mahmoudibrahem.omnicart.core.navigation.AppScreens
import com.mahmoudibrahem.omnicart.presentation.screens.auth.login.LoginScreen
import com.mahmoudibrahem.omnicart.presentation.screens.home.HomeScreen
import com.mahmoudibrahem.omnicart.presentation.screens.search_results.SearchResultsScreen
import com.mahmoudibrahem.omnicart.presentation.ui.theme.OmniCartTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val navController = rememberNavController()
            OmniCartTheme {
                AppNavigation(
                    navController = navController,
                    startDestination = AppScreens.Login.route,
                    host = this
                )
            }
        }
    }
}


