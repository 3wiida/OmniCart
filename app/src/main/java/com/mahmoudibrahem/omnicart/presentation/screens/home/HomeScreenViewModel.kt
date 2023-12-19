package com.mahmoudibrahem.omnicart.presentation.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.omnicart.R
import com.mahmoudibrahem.omnicart.core.util.Constants
import com.mahmoudibrahem.omnicart.core.util.DateParser.parseDate
import com.mahmoudibrahem.omnicart.core.util.onResponse
import com.mahmoudibrahem.omnicart.domain.model.Category
import com.mahmoudibrahem.omnicart.domain.usecase.GetHomeUseCase
import com.mahmoudibrahem.omnicart.domain.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getHomeUseCase: GetHomeUseCase,
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUIState())
    val uiState = _uiState.asStateFlow()
    private var searchJob: Job? = null

    init {
        getHome()
    }

    private fun getHome() {
        viewModelScope.launch(Dispatchers.IO) {
            getHomeUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, errorMsg = null) }
                },
                onFailure = { msg ->
                    _uiState.update { it.copy(errorMsg = msg) }
                },
                onSuccess = { response ->
                    val categoryList = prepareCategoriesList(response?.categories)
                    Constants.categories = categoryList
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMsg = null,
                            topSalesList = response!!.topSales,
                            freshSalesList = response.freshSales,
                            recommended = response.recommended,
                            categoryList = categoryList
                        )
                    }
                }
            )
        }
    }

    fun onSearchQueryChanged(newValue: String) {
        _uiState.update { it.copy(searchQuery = newValue) }
        if (newValue.isEmpty()) {
            searchJob?.cancel()
            _uiState.update { it.copy(searchResultsList = emptyList()) }
        } else {
            searchForProduct()
        }
    }

    private fun searchForProduct() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            searchUseCase(query = uiState.value.searchQuery).onResponse(
                onLoading = {},
                onSuccess = { results ->
                    _uiState.update { it.copy(searchResultsList = results!!.map { product -> product.name }) }
                },
                onFailure = {}
            )
        }
    }

    private fun prepareCategoriesList(categories: List<String>?): List<Category> {
        val categoryList = mutableListOf<Category>()
        categories?.forEach { categoryName ->
            when (categoryName) {
                "fashion" -> {
                    categoryList.add(
                        Category(
                            name = categoryName,
                            icon = R.drawable.fashion_icon
                        )
                    )
                }


                "watches" -> {
                    categoryList.add(
                        Category(
                            name = categoryName,
                            icon = R.drawable.watch_icon
                        )
                    )
                }


                "footwear" -> {
                    categoryList.add(
                        Category(
                            name = categoryName,
                            icon = R.drawable.shoes_icon
                        )
                    )
                }


                "computers" -> {
                    categoryList.add(
                        Category(
                            name = categoryName,
                            icon = R.drawable.computer_icon
                        )
                    )
                }


                "sports" -> {
                    categoryList.add(
                        Category(
                            name = categoryName,
                            icon = R.drawable.sports_icon
                        )
                    )
                }

                "home" -> {
                    categoryList.add(
                        Category(
                            name = categoryName,
                            icon = R.drawable.home_icon
                        )
                    )
                }

                "mobiles" -> {
                    categoryList.add(
                        Category(
                            name = categoryName,
                            icon = R.drawable.mobile_icon
                        )
                    )
                }


            }
        }
        return categoryList
    }

}