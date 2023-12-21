package com.mahmoudibrahem.omnicart.presentation.screens.auth.forget_password

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
import androidx.compose.runtime.LaunchedEffect
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
fun EmailEnterScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onNavigateToOTPEnter: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    EmailEnterScreenContent(
        uiState = uiState,
        onSendCodeClicked = viewModel::sendOTPEmail
    )
    LaunchedEffect(key1 = uiState.otp) {
        if (uiState.otp != null) {
            onNavigateToOTPEnter(uiState.otp!!)
        }
    }
}

@Composable
private fun EmailEnterScreenContent(
    uiState: ForgotPasswordUIState,
    onSendCodeClicked: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
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
                painter = painterResource(id = R.drawable.email_person),
                contentDescription = ""
            )
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "Who are You?",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium,
                fontSize = 24.sp
            )
            Text(
                modifier = Modifier.padding(bottom = 24.dp),
                text = stringResource(R.string.enter_your_email_to_send_the_verification_code),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            MainTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp),
                value = email,
                onValueChanged = { newValue ->
                    email = newValue
                },
                placeHolder = stringResource(R.string.enter_your_email)
            )
            MainButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                text = stringResource(R.string.send_code),
                isEnabled = !uiState.isSendEmailLoading && email.isNotEmpty(),
                isLoading = uiState.isSendEmailLoading,
                onClick = {
                    onSendCodeClicked(email)
                }
            )
        }
    }
}

@Preview
@Composable
private fun EmailEnterScreenPreview() {
    EmailEnterScreen()
}