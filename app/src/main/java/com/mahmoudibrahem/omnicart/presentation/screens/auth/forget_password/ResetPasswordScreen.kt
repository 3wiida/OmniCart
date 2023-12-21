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
fun ResetPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit = {},
    token: String = ""
) {
    val uiState by viewModel.uiState.collectAsState()
    ResetPasswordScreenContent(
        uiState = uiState,
        token = token,
        onResetClicked = viewModel::resetPassword
    )
    LaunchedEffect(key1 = uiState.isResetPasswordSucceeded) {
        if (uiState.isResetPasswordSucceeded) onNavigateToLogin()
    }
}

@Composable
private fun ResetPasswordScreenContent(
    uiState: ForgotPasswordUIState,
    token: String,
    onResetClicked: (String, String, String) -> Unit
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
                painter = painterResource(id = R.drawable.reset_password),
                contentDescription = ""
            )
            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = "Reset Password",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.labelMedium,
                fontSize = 24.sp
            )
            Text(
                modifier = Modifier.padding(bottom = 24.dp),
                text = stringResource(R.string.enter_the_new_password_make_sure_that_it_s_strong),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
            MainTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                value = password,
                onValueChanged = { newValue ->
                    password = newValue
                },
                isPassword = true,
                placeHolder = stringResource(R.string.password)
            )
            MainTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp),
                value = confirmPassword,
                onValueChanged = { newValue ->
                    confirmPassword = newValue
                },
                isPassword = true,
                placeHolder = stringResource(id = R.string.confirm_password)
            )
            MainButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                text = stringResource(R.string.reset),
                isEnabled = !uiState.isResetPasswordLoading && password == confirmPassword && password.length > 7,
                isLoading = uiState.isResetPasswordLoading,
                onClick = { onResetClicked(token, password, confirmPassword) },
            )
        }
    }
}

@Preview
@Composable
private fun ResetPasswordScreenPreview() {
    ResetPasswordScreen()
}