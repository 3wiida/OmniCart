package com.mahmoudibrahem.omnicart.core.di

import android.util.Log
import com.mahmoudibrahem.omnicart.data.remote.OmniCartAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHTTPClient(): OkHttpClient {
        val httpClientLoggingInterceptor = HttpLoggingInterceptor { msg ->
            Log.i("NetworkInterceptor", "Result : $msg")
        }
        httpClientLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder().apply {
            addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                newRequest.addHeader("Accept", "application/json")
                //TODO ADD TOKEN HERE
                newRequest.addHeader("Authorization", "Bearer ")
                chain.proceed(newRequest.build())
            }
            addInterceptor(httpClientLoggingInterceptor)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            callTimeout(60, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    @Singleton
    fun provideAPI(client: OkHttpClient): OmniCartAPI {
        return Retrofit.Builder()
            .baseUrl("https://omnicart.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(OmniCartAPI::class.java)
    }


}