package com.mahmoudibrahem.omnicart.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.mahmoudibrahem.omnicart.core.util.Constants.IS_LOGGED_IN_KEY
import com.mahmoudibrahem.omnicart.core.util.Constants.TAG
import com.mahmoudibrahem.omnicart.core.util.Constants.USER_TOKEN_KEY
import com.mahmoudibrahem.omnicart.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository {

    override suspend fun <T> saveInDataStore(key: Preferences.Key<T>, value: T) {
        dataStore.edit {
            it[key] = value
        }
    }

    override suspend fun <T> getFromDataStore(key: Preferences.Key<T>): T? {
        return dataStore.data
            .catch { throwable -> Log.d(TAG, "getFromDataStore: $throwable") }
            .map { it[key] }
            .firstOrNull()
    }

}