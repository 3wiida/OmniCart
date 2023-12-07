package com.mahmoudibrahem.omnicart.core.di

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.mahmoudibrahem.omnicart.core.util.Constants
import com.mahmoudibrahem.omnicart.core.util.Constants.APP_DATA_STORE_NAME
import com.mahmoudibrahem.omnicart.data.remote.OmniCartAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private val Context.appDataStore by preferencesDataStore(name = APP_DATA_STORE_NAME)

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
                newRequest.addHeader("Authorization", "Bearer ${Constants.userToken}")
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


    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.appDataStore
    }

}