package com.mahmoudibrahem.omnicart.domain.repository

import com.mahmoudibrahem.omnicart.core.util.Resource
import com.mahmoudibrahem.omnicart.domain.model.CartActionResponse
import com.mahmoudibrahem.omnicart.domain.model.CommonProduct
import com.mahmoudibrahem.omnicart.domain.model.HomeResponse
import com.mahmoudibrahem.omnicart.domain.model.LoginResponse
import com.mahmoudibrahem.omnicart.domain.model.ProductData
import com.mahmoudibrahem.omnicart.domain.model.RegisterResponse
import com.mahmoudibrahem.omnicart.domain.model.SingleProductInfo
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {

    suspend fun login(
        email: String,
        password: String
    ): Flow<Resource<LoginResponse>>

    suspend fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Flow<Resource<RegisterResponse>>

    suspend fun getHome(): Flow<Resource<HomeResponse>>

    suspend fun search(query: String): Flow<Resource<List<CommonProduct>>>

    suspend fun getSingleProduct(id: String): Flow<Resource<ProductData>>

    suspend fun addToCart(productID: String): Flow<Resource<CartActionResponse>>

    suspend fun removeFromCart(productID: String): Flow<Resource<CartActionResponse>>

    suspend fun upsertInWishlist(productID: String): Flow<Resource<Unit>>

    suspend fun sendReview(
        productID: String,
        review: String,
        rating: Float
    ): Flow<Resource<Unit>>
}