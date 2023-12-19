package com.mahmoudibrahem.omnicart.data.repository

import android.util.Log
import com.mahmoudibrahem.omnicart.core.util.Resource
import com.mahmoudibrahem.omnicart.core.util.parseToErrorModel
import com.mahmoudibrahem.omnicart.data.remote.OmniCartAPI
import com.mahmoudibrahem.omnicart.domain.model.CartItem
import com.mahmoudibrahem.omnicart.domain.model.CommonProduct
import com.mahmoudibrahem.omnicart.domain.model.HomeResponse
import com.mahmoudibrahem.omnicart.domain.model.LoginResponse
import com.mahmoudibrahem.omnicart.domain.model.Order
import com.mahmoudibrahem.omnicart.domain.model.ProductData
import com.mahmoudibrahem.omnicart.domain.model.RegisterResponse
import com.mahmoudibrahem.omnicart.domain.model.SingleOrder
import com.mahmoudibrahem.omnicart.domain.model.User
import com.mahmoudibrahem.omnicart.domain.model.UserAddress
import com.mahmoudibrahem.omnicart.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val api: OmniCartAPI
) : NetworkRepository {
    override suspend fun login(email: String, password: String): Flow<Resource<LoginResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = api.login(email = email, password = password)
                emit(Resource.Success(data = response.toLoginResponse()))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Flow<Resource<RegisterResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = api.register(
                    name = name,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword
                )
                emit(Resource.Success(data = response.toRegisterResponse()))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun getHome(): Flow<Resource<HomeResponse>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = api.getHome()
                emit(Resource.Success(data = response.toHomeResponse()))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                Log.d("```TAG```", "getHome: $e")
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun search(query: String): Flow<Resource<List<CommonProduct>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = api.search(query = query)
                emit(Resource.Success(data = response.data.products.map { it.toCommonProduct() }))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun getSingleProduct(id: String): Flow<Resource<ProductData>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = api.getSingleProduct(id = id)
                emit(Resource.Success(data = response.toProductData()))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun addToCart(productID: String): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            try {
                api.addProductToCart(productID = productID)
                emit(Resource.Success(data = Unit))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun removeFromCart(productID: String): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            try {
                api.deleteFromCart(productID = productID)
                emit(Resource.Success(data = Unit))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun upsertInWishlist(productID: String): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            try {
                api.upsertInWishlist(productID = productID)
                emit(Resource.Success(data = Unit))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun sendReview(
        productID: String,
        review: String,
        rating: Float
    ): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            try {
                api.sendReview(
                    productID = productID,
                    review = review,
                    rating = rating
                )
                emit(Resource.Success(data = Unit))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun getMyCart(): Flow<Resource<List<CartItem>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = api.getMyCart()
                emit(Resource.Success(data = response.data.cart.items.map { it.toCartItem() }))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun decreaseQuantityOfItem(productID: String): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            try {
                api.decreaseQuantityOfItem(
                    productID = productID,
                )
                emit(Resource.Success(data = Unit))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun addAddress(
        addressName: String,
        country: String,
        city: String,
        addressLine1: String,
        addressLine2: String,
        zipCode: String,
        phoneNumber: String
    ): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            try {
                api.addAddress(
                    addressName = addressName,
                    country = country,
                    city = city,
                    addressLine1 = addressLine1,
                    addressLine2 = addressLine2,
                    zipCode = zipCode,
                    phoneNumber = phoneNumber
                )
                emit(Resource.Success(data = Unit))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun getUserAddress(): Flow<Resource<List<UserAddress>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = api.getUserAddress()
                emit(Resource.Success(data = response.address.map { it.toUserAddress() }))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun getWishlist(): Flow<Resource<List<CommonProduct>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = api.getWishlist()
                emit(Resource.Success(data = response.data.wishlists.map { it.toCommonProduct() }))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun completeOrder(): Flow<Resource<Unit>> {
        return flow {
            emit(Resource.Loading())
            try {
                api.completeOrder()
                emit(Resource.Success(data = Unit))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun getMyOrders(): Flow<Resource<List<Order>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = api.getMyOrders()
                emit(Resource.Success(data = response.orders.map { it.toOrder() }))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun getSingleOrderDetails(orderID: String): Flow<Resource<SingleOrder>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = api.getSingleOrderDetails(orderID = orderID)
                emit(Resource.Success(data = response.order.toSingleOrder()))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                Log.d("```TAG```", "getSingleOrderDetails: $e")
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun getMyInfo(): Flow<Resource<User>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = api.getMyInfo()
                emit(Resource.Success(data = response.toUser()))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun getOfferProducts(): Flow<Resource<List<CommonProduct>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = api.getOfferProducts()
                emit(Resource.Success(data = response.data.product.map { it.toCommonProduct() }))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }

    override suspend fun getCategoryProducts(categoryName: String): Flow<Resource<List<CommonProduct>>> {
        return flow {
            emit(Resource.Loading())
            try {
                val response = api.getCategoryProducts(categoryName = categoryName)
                emit(Resource.Success(data = response.data.products.map { it.toCommonProduct() }))
            } catch (e: HttpException) {
                val error = e.parseToErrorModel()
                emit(Resource.Failure(message = error.message))
            } catch (e: IOException) {
                emit(Resource.Failure(message = "Can't reach server, check your internet connection"))
            } catch (e: Exception) {
                emit(Resource.Failure(message = "Unknown error happened, try again later"))
            }
        }
    }
}