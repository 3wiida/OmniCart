package com.mahmoudibrahem.omnicart.domain.repository

import com.mahmoudibrahem.omnicart.core.util.Resource
import com.mahmoudibrahem.omnicart.domain.model.CommonProduct
import com.mahmoudibrahem.omnicart.domain.model.HomeResponse
import com.mahmoudibrahem.omnicart.domain.model.LoginResponse
import com.mahmoudibrahem.omnicart.domain.model.RegisterResponse
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
}