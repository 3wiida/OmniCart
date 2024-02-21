package com.mahmoudibrahem.omnicart.presentation.screens.user_address

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.omnicart.core.util.onResponse
import com.mahmoudibrahem.omnicart.domain.usecase.CompleteOrderUseCase
import com.mahmoudibrahem.omnicart.domain.usecase.GetPaymentInfoUseCase
import com.mahmoudibrahem.omnicart.domain.usecase.GetUserAddressUseCase
import com.mahmoudibrahem.omnicart.presentation.MainActivity.Companion.isPaymentCompleted
import com.mahmoudibrahem.omnicart.presentation.MainActivity.Companion.paymentLauncher
import com.stripe.android.PaymentConfiguration
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.PaymentMethodCreateParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAddressViewModel @Inject constructor(
    private val getUserAddressUseCase: GetUserAddressUseCase,
    private val completeOrderUseCase: CompleteOrderUseCase,
    private val getPaymentInfoUseCase: GetPaymentInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserAddressScreenUIState())
    val uiState = _uiState.asStateFlow()

    init {
        checkPaymentState()
    }

    fun onAddressSelected(addressIndex: Int) {
        _uiState.update { it.copy(selectedAddress = addressIndex) }
    }

    private fun completeOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            completeOrderUseCase()
                .onResponse(
                    onLoading = {
                        _uiState.update { it.copy(isButtonLoading = true) }
                    },
                    onSuccess = {
                        _uiState.update {
                            it.copy(
                                isButtonLoading = false,
                                isOrderCompleted = true
                            )
                        }
                    },
                    onFailure = {
                        _uiState.update { it.copy(isButtonLoading = false) }
                    }
                )
        }
    }

    fun getUserAddress() {
        viewModelScope.launch(Dispatchers.IO) {
            getUserAddressUseCase()
                .onResponse(
                    onLoading = {
                        _uiState.update { it.copy(isLoading = true) }
                    },
                    onSuccess = { response ->
                        _uiState.update { it.copy(isLoading = false, addressList = response!!) }
                    },
                    onFailure = {}
                )
        }
    }

    fun getPaymentInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            getPaymentInfoUseCase()
                .onResponse(
                    onLoading = {
                        _uiState.update { it.copy(isButtonLoading = true) }
                    },
                    onSuccess = { info ->
                        Log.d("````TAG````", "getPaymentInfo: $info")
                        _uiState.update { it.copy(paymentInfo = info, isButtonLoading = false) }
                    },
                    onFailure = {
                        Log.d("````TAG````", "getPaymentInfo: $it")
                        _uiState.update { it.copy(isButtonLoading = false) }
                    }
                )
        }
    }

    fun onPaymentConfirmed(params: PaymentMethodCreateParams) {
        val confirmParams = ConfirmPaymentIntentParams
            .createWithPaymentMethodCreateParams(
                params,
                uiState.value.paymentInfo!!.clientSecret
            )
        viewModelScope.launch {
            paymentLauncher.confirm(confirmParams)
        }
    }

    private fun checkPaymentState() {
        viewModelScope.launch {
            isPaymentCompleted.collectLatest { isCompleted ->
                if (isCompleted) {
                    completeOrder()
                    isPaymentCompleted.value = false
                }
            }
        }
    }

}