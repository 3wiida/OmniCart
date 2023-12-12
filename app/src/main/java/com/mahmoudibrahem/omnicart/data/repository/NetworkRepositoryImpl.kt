package com.mahmoudibrahem.omnicart.data.repository

import com.mahmoudibrahem.omnicart.core.util.Resource
import com.mahmoudibrahem.omnicart.core.util.parseToErrorModel
import com.mahmoudibrahem.omnicart.data.remote.OmniCartAPI
import com.mahmoudibrahem.omnicart.domain.model.CartItem
import com.mahmoudibrahem.omnicart.domain.model.CommonProduct
import com.mahmoudibrahem.omnicart.domain.model.HomeResponse
import com.mahmoudibrahem.omnicart.domain.model.LoginResponse
import com.mahmoudibrahem.omnicart.domain.model.ProductData
import com.mahmoudibrahem.omnicart.domain.model.RegisterResponse
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
}