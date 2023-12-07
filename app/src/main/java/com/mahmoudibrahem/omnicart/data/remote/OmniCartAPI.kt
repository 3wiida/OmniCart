package com.mahmoudibrahem.omnicart.data.remote

import com.mahmoudibrahem.omnicart.data.remote.dto.CartActionResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.HomeResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.LoginResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.ProductInfoDto
import com.mahmoudibrahem.omnicart.data.remote.dto.RegisterResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.SearchResponseDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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
        @Query("search") query: String
    ): SearchResponseDto

    @GET("{product_id}")
    suspend fun getSingleProduct(
        @Path("product_id") id: String
    ): ProductInfoDto

    @PUT("add-to-cart/{product_id}")
    suspend fun addProductToCart(
        @Path("product_id") productID: String
    ):CartActionResponseDto


    @DELETE("remove-from-cart/{product_id}")
    suspend fun deleteFromCart(
        @Path("product_id") productID:String
    ):CartActionResponseDto

    @POST("wishlist/{product_id}")
    suspend fun upsertInWishlist(
        @Path("product_id") productID: String
    )
}