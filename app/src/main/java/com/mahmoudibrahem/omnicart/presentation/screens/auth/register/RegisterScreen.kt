package com.mahmoudibrahem.omnicart.presentation.screens.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.presentation.components.MainButton
import com.mahmoudibrahem.omnicart.presentation.components.MainTextField

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit = {},
    onNavigateToHome: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    RegisterScreenContent(
        uiState = uiState,
        onNameChanged = viewModel::onNameChanged,
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onConfirmPasswordChanged = viewModel::onConfirmPasswordChanged,
        onRegisterClicked = viewModel::onRegisterButtonClicked,
        onSignInClicked = onNavigateToLogin
    )
    LaunchedEffect(key1 = uiState.isRegisterSuccessful){
        if(uiState.isRegisterSuccessful){
            onNavigateToHome()
        }
    }
}

@Composable
private fun RegisterScreenContent(
    uiState: RegisterScreenUIState,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onRegisterClicked: () -> Unit,
    onSignInClicked: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        item {
            RegisterScreenHeader()
        }
        item {
            RegisterDataSection(
                name = uiState.name,
                email = uiState.email,
                password = uiState.password,
                confirmPassword = uiState.confirmPassword,
                isButtonLoading = uiState.isLoading,
                onNameChanged = onNameChanged,
                onEmailChanged = onEmailChanged,
                onPasswordChanged = onPasswordChanged,
                onConfirmPasswordChanged = onConfirmPasswordChanged,
                onRegisterClicked = onRegisterClicked
            )
        }
        item {
            RegisterScreenFooter(
                onSignInClicked = onSignInClicked
            )
        }
    }
}

@Composable
private fun RegisterScreenHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.padding(bottom = 16.dp, top = 68.dp),
            painter = painterResource(id = R.drawable.blue_app_icon),
            contentDescription = stringResource(R.string.app_icon),
            tint = Color.Unspecified
        )
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(R.string.let_s_get_started),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.create_an_new_account),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RegisterDataSection(
    name: String,
    email: String,
    password: String,
    confirmPassword: String,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    isButtonLoading: Boolean,
    onRegisterClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp)
    ) {
        MainTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            value = name,
            onValueChanged = onNameChanged,
            placeHolder = stringResource(R.string.full_name),
            leadingIcon = R.drawable.person_icon
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        MainTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            value = email,
            onValueChanged = onEmailChanged,
            placeHolder = stringResource(R.string.your_email),
            leadingIcon = R.drawable.email_icon
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        MainTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            value = password,
            onValueChanged = onPasswordChanged,
            isPassword = true,
            placeHolder = stringResource(R.string.password),
            leadingIcon = R.drawable.password_icon
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        MainTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            value = confirmPassword,
            onValueChanged = onConfirmPasswordChanged,
            isPassword = true,
            placeHolder = stringResource(R.string.confirm_password),
            leadingIcon = R.drawable.password_icon
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        MainButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            text = stringResource(R.string.sign_up),
            isLoading = isButtonLoading,
            isEnabled = !isButtonLoading &&
                    email.isNotEmpty() &&
                    password.isNotEmpty() &&
                    name.isNotEmpty() &&
                    confirmPassword.isNotEmpty(),
            onClick = onRegisterClicked
        )
    }
}

@Composable
private fun RegisterScreenFooter(
    onSignInClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.have_a_account),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ClickableText(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(color = MaterialTheme.colorScheme.primary)
                ) {
                    append(stringResource(R.string.sign_in))
                }
            },
            style = MaterialTheme.typography.labelSmall,
            onClick = { onSignInClicked() }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    RegisterScreen()
}