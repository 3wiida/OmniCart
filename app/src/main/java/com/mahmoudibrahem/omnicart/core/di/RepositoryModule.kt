package com.mahmoudibrahem.omnicart.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.mahmoudibrahem.omnicart.data.remote.OmniCartAPI
import com.mahmoudibrahem.omnicart.data.repository.DataStoreRepositoryImpl
import com.mahmoudibrahem.omnicart.data.repository.NetworkRepositoryImpl
import com.mahmoudibrahem.omnicart.domain.repository.DataStoreRepository
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

    @Provides
    @Singleton
    fun provideDataStoreRepository(dataStore: DataStore<Preferences>): DataStoreRepository {
        return DataStoreRepositoryImpl(dataStore)
    }

}