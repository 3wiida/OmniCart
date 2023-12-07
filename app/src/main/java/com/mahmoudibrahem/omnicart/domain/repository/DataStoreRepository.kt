package com.mahmoudibrahem.omnicart.domain.repository

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun <T> saveInDataStore(key: Preferences.Key<T>, value: T)

    suspend fun <T> getFromDataStore(key: Preferences.Key<T>): T?

}