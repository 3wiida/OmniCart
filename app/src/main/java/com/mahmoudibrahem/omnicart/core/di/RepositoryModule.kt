package com.mahmoudibrahem.omnicart.core.di

import com.mahmoudibrahem.omnicart.data.remote.OmniCartAPI
import com.mahmoudibrahem.omnicart.data.repository.NetworkRepositoryImpl
import com.mahmoudibrahem.omnicart.domain.repository.NetworkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNetworkRepository(api: OmniCartAPI): NetworkRepository {
        return NetworkRepositoryImpl(api)
    }

}