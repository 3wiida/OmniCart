package com.mahmoudibrahem.omnicart.presentation.screens.auth.forget_password

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.presentation.components.MainButton
import com.mahmoudibrahem.omnicart.presentation.components.MainTextField

@Composable
fun OTPEnterScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onNavigateToResetPassword: (String) -> Unit = {},
    correctOTP: String = ""
) {
    val uiState by viewModel.uiState.collectAsState()
    OTPEnterScreenContent(
        uiState = uiState,
        correctOTP = correctOTP,
        onNextClicked = { otp ->
            if (viewModel.checkOTPMatching(otp, correctOTP)) onNavigateToResetPassword(correctOTP)
        }
    )
}

@Composable
private fun OTPEnterScreenContent(
    uiState: ForgotPasswordUIState,
    onNextClicked: (String) -> Unit,
    correctOTP: String
) {
    var otp by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(200.dp),
                painter = painterResource(id = R.drawable.otp_person),
                contentDescription = ""
            )
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = stringResource(R.string.otp_code),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium,
                fontSize = 24.sp
            )
            Text(
                modifier = Modifier.padding(bottom = 24.dp),
                text = stringResource(R.string.enter_the_otp_code_you_received_on_your_email),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            MainTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp),
                value = otp,
                onValueChanged = { newValue ->
                    if (newValue.length < 7) otp = newValue
                },
                placeHolder = stringResource(R.string.enter_the_otp_code),
                error = error
            )
            MainButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                text = stringResource(id = R.string.next),
                isEnabled = otp.length > 5,
                onClick = {
                    Log.d("```TAG```", "OTPEnterScreenContent: $otp ---- ${uiState.otp}")
                    if (otp != correctOTP) error = "Incorrect OTP Code"
                    onNextClicked(otp)
                }
            )
        }
    }
}


@Preview
@Composable
private fun OTPEnterScreenPreview() {
    OTPEnterScreen()
}