package com.mahmoudibrahem.omnicart.data.remote

import com.mahmoudibrahem.omnicart.data.remote.dto.CartResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.CategoryProductsResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.HomeResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.LoginResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.MeResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.OTPResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.OfferResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.OrdersResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.ProductInfoDto
import com.mahmoudibrahem.omnicart.data.remote.dto.RegisterResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.SearchResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.SingleOrderResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.UserAddressResponseDto
import com.mahmoudibrahem.omnicart.data.remote.dto.WishlistResponseDto
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
    )


    @DELETE("remove-from-cart/{product_id}")
    suspend fun deleteFromCart(
        @Path("product_id") productID: String
    )

    @POST("wishlist/{product_id}")
    suspend fun upsertInWishlist(
        @Path("product_id") productID: String
    )

    @POST("{product_id}/reviews/add-review")
    suspend fun sendReview(
        @Path("product_id") productID: String,
        @Query("rating") rating: Float,
        @Query("review") review: String
    )

    @GET("my-cart")
    suspend fun getMyCart(): CartResponseDto

    @PUT("decrease-quantity/{product_id}")
    suspend fun decreaseQuantityOfItem(
        @Path("product_id") productID: String
    )

    @POST("address")
    suspend fun addAddress(
        @Query("name") addressName: String,
        @Query("country") country: String,
        @Query("city") city: String,
        @Query("street") addressLine1: String,
        @Query("street2") addressLine2: String,
        @Query("zipcode") zipCode: String,
        @Query("phoneNumber") phoneNumber: String,
    )

    @GET("user-address")
    suspend fun getUserAddress(): UserAddressResponseDto

    @GET("My-wishlist")
    suspend fun getWishlist(): WishlistResponseDto

    @POST("add-order")
    suspend fun completeOrder()

    @GET("My-orders")
    suspend fun getMyOrders(): OrdersResponseDto

    @GET("order-detail/{order_id}")
    suspend fun getSingleOrderDetails(
        @Path("order_id") orderID: String
    ): SingleOrderResponseDto

    @GET("me")
    suspend fun getMyInfo(): MeResponseDto

    @GET("sales")
    suspend fun getOfferProducts(): OfferResponseDto

    @GET("categories/{category_name}")
    suspend fun getCategoryProducts(
        @Path("category_name") categoryName: String
    ): CategoryProductsResponseDto

    @POST("forgetpassword")
    suspend fun getResetPasswordOTP(
        @Query("email") email: String
    ): OTPResponseDto

    @POST("resetpassword")
    suspend fun resetPassword(
        @Query("token") token: String,
        @Query("password") password: String,
        @Query("confirmPassword") confirmPassword: String
    )
}