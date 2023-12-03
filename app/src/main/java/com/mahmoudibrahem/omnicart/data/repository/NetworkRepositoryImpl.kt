package com.mahmoudibrahem.omnicart.data.repository

import com.mahmoudibrahem.omnicart.core.util.Resource
import com.mahmoudibrahem.omnicart.core.util.parseToErrorModel
import com.mahmoudibrahem.omnicart.data.remote.OmniCartAPI
import com.mahmoudibrahem.omnicart.domain.model.CommonProduct
import com.mahmoudibrahem.omnicart.domain.model.HomeResponse
import com.mahmoudibrahem.omnicart.domain.model.LoginResponse
import com.mahmoudibrahem.omnicart.domain.model.RegisterResponse
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
}