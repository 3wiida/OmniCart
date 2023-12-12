package com.mahmoudibrahem.omnicart.presentation.screens.add_address

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.presentation.components.MainButton
import com.mahmoudibrahem.omnicart.presentation.components.MainTextField

@Composable
fun AddAddressScreen(
    viewModel: AddAddressViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    AddAddressScreenContent(
        uiState = uiState,
        onBackClicked = onNavigateBack,
        onAddressNameChanged = viewModel::onAddressNameChanged,
        onCountryChanged = viewModel::onCountryChanged,
        onCityChanged = viewModel::onCityChanged,
        onAddressLine1Changed = viewModel::onAddressLine1Changed,
        onAddressLine2Changed = viewModel::onAddressLine2Changed,
        onZipCodeChanged = viewModel::onZipCodeChanged,
        onPhoneNumberChanged = viewModel::onPhoneNumberChanged,
        onAddAddressClicked = viewModel::addAddress
    )
}

@Composable
private fun AddAddressScreenContent(
    uiState: AddAddressScreenUIState,
    onBackClicked: () -> Unit,
    onAddressNameChanged: (String) -> Unit,
    onCountryChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onAddressLine1Changed: (String) -> Unit,
    onAddressLine2Changed: (String) -> Unit,
    onZipCodeChanged: (String) -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
    onAddAddressClicked: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 26.dp)
        ) {
            ScreenHeader(
                onBackClicked = onBackClicked
            )
            AddressDetailsSection(
                addressName = uiState.addressName,
                country = uiState.country,
                city = uiState.city,
                addressLine1 = uiState.addressLine1,
                addressLine2 = uiState.addressLine2,
                zipCode = uiState.zipCode,
                phoneNumber = uiState.phoneNumber,
                isAddButtonLoading = uiState.isAddButtonLoading,
                onAddressNameChanged = onAddressNameChanged,
                onCountryChanged = onCountryChanged,
                onCityChanged = onCityChanged,
                onAddressLine1Changed = onAddressLine1Changed,
                onAddressLine2Changed = onAddressLine2Changed,
                onZipCodeChanged = onZipCodeChanged,
                onPhoneNumberChanged = onPhoneNumberChanged,
                onAddAddressClicked = onAddAddressClicked
            )

        }
    }
}

@Composable
private fun ScreenHeader(
    onBackClicked: () -> Unit
) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClicked
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_icon),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
            }
            Text(
                text = stringResource(R.string.add_address),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Divider(
            modifier = Modifier.padding(top = 8.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
        )
    }
}

@Composable
private fun AddressDetailsSection(
    addressName: String,
    country: String,
    city: String,
    addressLine1: String,
    addressLine2: String,
    zipCode: String,
    phoneNumber: String,
    isAddButtonLoading: Boolean,
    onAddressNameChanged: (String) -> Unit,
    onCountryChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onAddressLine1Changed: (String) -> Unit,
    onAddressLine2Changed: (String) -> Unit,
    onZipCodeChanged: (String) -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
    onAddAddressClicked: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
    ) {
        item {
            Column {
                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 12.dp),
                    text = stringResource(R.string.address_name),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelMedium
                )
                MainTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = addressName,
                    onValueChanged = onAddressNameChanged,
                    placeHolder = stringResource(R.string.ex_home_address)
                )
            }
        }
        item {
            Column {
                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 12.dp),
                    text = stringResource(R.string.country),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelMedium
                )
                MainTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = country,
                    onValueChanged = onCountryChanged,
                    placeHolder = stringResource(R.string.ex_egypt)
                )
            }
        }
        item {
            Column {
                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 12.dp),
                    text = stringResource(R.string.city),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelMedium
                )
                MainTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = city,
                    onValueChanged = onCityChanged,
                    placeHolder = stringResource(R.string.ex_cairo),
                )
            }
        }
        item {
            Column {
                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 12.dp),
                    text = stringResource(R.string.address_line_1),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelMedium
                )
                MainTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = addressLine1,
                    onValueChanged = onAddressLine1Changed,
                    placeHolder = stringResource(R.string.ex_faisal_giza)
                )
            }
        }
        item {
            Column {
                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 12.dp),
                    text = stringResource(R.string.address_line_2),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelMedium
                )
                MainTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = addressLine2,
                    onValueChanged = onAddressLine2Changed,
                    placeHolder = stringResource(R.string.ex_diaa_street)
                )
            }
        }
        item {
            Column {
                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 12.dp),
                    text = stringResource(R.string.zip_code),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelMedium
                )
                MainTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = zipCode,
                    onValueChanged = onZipCodeChanged,
                    placeHolder = stringResource(R.string.ex_12345)
                )
            }
        }
        item {
            Column {
                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 12.dp),
                    text = stringResource(R.string.phone_number),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.labelMedium
                )
                MainTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = phoneNumber,
                    onValueChanged = onPhoneNumberChanged,
                    placeHolder = stringResource(R.string.ex_01x_xxxx_xxx)
                )
            }
        }
        item {
            MainButton(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                text = stringResource(R.string.add_address),
                onClick = onAddAddressClicked,
                isLoading = isAddButtonLoading,
                isEnabled = !isAddButtonLoading &&
                        addressName.isNotEmpty() &&
                        country.isNotEmpty() &&
                        city.isNotEmpty() &&
                        addressLine1.isNotEmpty() &&
                        addressLine2.isNotEmpty() &&
                        zipCode.isNotEmpty() &&
                        phoneNumber.isNotEmpty()
            )
        }
    }
}

@Preview
@Composable
private fun AddAddressScreenPreview() {
    AddAddressScreen()
}