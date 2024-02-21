package com.mahmoudibrahem.omnicart.domain.repository

import com.mahmoudibrahem.omnicart.core.util.Resource
import com.mahmoudibrahem.omnicart.domain.model.CartItem
import com.mahmoudibrahem.omnicart.domain.model.CommonProduct
import com.mahmoudibrahem.omnicart.domain.model.HomeResponse
import com.mahmoudibrahem.omnicart.domain.model.LoginResponse
import com.mahmoudibrahem.omnicart.domain.model.Order
import com.mahmoudibrahem.omnicart.domain.model.PaymentInfo
import com.mahmoudibrahem.omnicart.domain.model.ProductData
import com.mahmoudibrahem.omnicart.domain.model.RegisterResponse
import com.mahmoudibrahem.omnicart.domain.model.SingleOrder
import com.mahmoudibrahem.omnicart.domain.model.User
import com.mahmoudibrahem.omnicart.domain.model.UserAddress
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

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

    suspend fun sendReview(
        productID: String,
        review: String,
        rating: Float
    ): Flow<Resource<Unit>>

    suspend fun addAddress(
        addressName: String,
        country: String,
        city: String,
        addressLine1: String,
        addressLine2: String,
        zipCode: String,
        phoneNumber: String
    ): Flow<Resource<Unit>>

    suspend fun getHome(): Flow<Resource<HomeResponse>>

    suspend fun search(query: String): Flow<Resource<List<CommonProduct>>>

    suspend fun getSingleProduct(id: String): Flow<Resource<ProductData>>

    suspend fun addToCart(productID: String): Flow<Resource<Unit>>

    suspend fun removeFromCart(productID: String): Flow<Resource<Unit>>

    suspend fun upsertInWishlist(productID: String): Flow<Resource<Unit>>

    suspend fun getMyCart(): Flow<Resource<List<CartItem>>>

    suspend fun decreaseQuantityOfItem(productID: String): Flow<Resource<Unit>>

    suspend fun getUserAddress(): Flow<Resource<List<UserAddress>>>

    suspend fun getWishlist(): Flow<Resource<List<CommonProduct>>>

    suspend fun completeOrder(): Flow<Resource<Unit>>

    suspend fun getMyOrders(): Flow<Resource<List<Order>>>

    suspend fun getSingleOrderDetails(orderID: String): Flow<Resource<SingleOrder>>

    suspend fun getMyInfo(): Flow<Resource<User>>

    suspend fun getOfferProducts(): Flow<Resource<List<CommonProduct>>>

    suspend fun getCategoryProducts(categoryName: String): Flow<Resource<List<CommonProduct>>>

    suspend fun getResetPasswordOTP(email: String): Flow<Resource<String>>

    suspend fun resetPassword(
        token: String,
        password: String,
        confirmPassword: String
    ): Flow<Resource<Unit>>

    suspend fun getPaymentInfo():Flow<Resource<PaymentInfo>>
}