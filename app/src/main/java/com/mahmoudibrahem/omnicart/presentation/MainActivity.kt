package com.mahmoudibrahem.omnicart.presentation

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.mahmoudibrahem.omnicart.core.navigation.AppNavigation
import com.mahmoudibrahem.omnicart.core.navigation.AppScreens
import com.mahmoudibrahem.omnicart.core.util.Constants.isLoggedIn
import com.mahmoudibrahem.omnicart.presentation.ui.theme.OmniCartTheme
import com.stripe.android.PaymentConfiguration
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val isKeepSplashScreen = MutableLiveData(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delaySplashScreen()
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
        val paymentConfiguration = PaymentConfiguration.getInstance(applicationContext)
        paymentLauncher = PaymentLauncher.Companion.create(
            activity = this,
            publishableKey = paymentConfiguration.publishableKey,
            stripeAccountId = paymentConfiguration.stripeAccountId,
            callback = ::onPaymentResult
        )
    }

    private fun onPaymentResult(paymentResult: PaymentResult) {
        when (paymentResult) {
            is PaymentResult.Completed -> {
                paymentProcessState.value = PaymentState.Success
                isPaymentCompleted.value = true
                lifecycleScope.launch {
                    delay(1000)
                    paymentProcessState.value = PaymentState.Idle
                    isPaymentCompleted.value = false
                }
            }

            is PaymentResult.Failed -> {
                Toast.makeText(this, paymentResult.throwable.message, Toast.LENGTH_SHORT).show()
                paymentProcessState.value = PaymentState.Failure
            }

            is PaymentResult.Canceled -> {
            }

        }

    }

    private fun delaySplashScreen() {
        lifecycleScope.launch {
            delay(2000)
            isKeepSplashScreen.value = false
        }
    }

    companion object {
        lateinit var paymentLauncher: PaymentLauncher
        val isPaymentCompleted = MutableStateFlow(false)
        var paymentProcessState = MutableStateFlow(PaymentState.Idle)

        enum class PaymentState {
            Idle,
            Loading,
            Success,
            Failure
        }
    }
}


