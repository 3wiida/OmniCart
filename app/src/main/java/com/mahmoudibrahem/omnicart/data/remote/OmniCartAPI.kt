package com.mahmoudibrahem.omnicart.data.remote

import com.mahmoudibrahem.omnicart.data.remote.dto.HomeResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.LoginResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.RegisterResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.SearchResponseDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OmniCartAPI {


    @POST("login")
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): LoginResponseDto


    @POST("signup")
    suspend fun register(
        @Query("name") name: String,
        @Query("email") email: String,
        @Query("password") password: String,
        @Query("confirmPassword") confirmPassword: String,
    ): RegisterResponseDto

    @GET("home")
    suspend fun getHome(): HomeResponseDto

    @GET("search")
    suspend fun search(
        @Query("search") query:String
    ):SearchResponseDto

}