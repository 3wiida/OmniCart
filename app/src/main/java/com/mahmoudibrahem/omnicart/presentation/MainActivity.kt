package com.mahmoudibrahem.omnicart.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.mahmoudibrahem.omnicart.core.navigation.AppNavigation
import com.mahmoudibrahem.omnicart.core.navigation.AppScreens
import com.mahmoudibrahem.omnicart.core.util.Constants.isLoggedIn
import com.mahmoudibrahem.omnicart.presentation.components.RatingBar
import com.mahmoudibrahem.omnicart.presentation.screens.auth.forget_password.EmailEnterScreen
import com.mahmoudibrahem.omnicart.presentation.screens.auth.forget_password.OTPEnterScreen
import com.mahmoudibrahem.omnicart.presentation.screens.auth.forget_password.ResetPasswordScreen
import com.mahmoudibrahem.omnicart.presentation.ui.theme.OmniCartTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val isKeepSplashScreen = MutableLiveData(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delaySplashScreen(500)
        installSplashScreen().setKeepOnScreenCondition {
            isKeepSplashScreen.value ?: true
        }
        setContent {
            val navController = rememberNavController()
            val startDestination = if (isLoggedIn) AppScreens.Home.route else AppScreens.Login.route
            OmniCartTheme {
                AppNavigation(
                    navController = navController,
                    startDestination = startDestination
                )
            }
        }
    }

    private fun delaySplashScreen(duration: Long) {
        lifecycleScope.launch {
            delay(duration)
            isKeepSplashScreen.value = false
        }
    }
}


