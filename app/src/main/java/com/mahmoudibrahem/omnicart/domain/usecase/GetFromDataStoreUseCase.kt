package com.mahmoudibrahem.omnicart.domain.usecase

import androidx.datastore.preferences.core.Preferences
import com.mahmoudibrahem.omnicart.domain.repository.DataStoreRepository
import javax.inject.Inject

class GetFromDataStoreUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {

    suspend operator fun <T> invoke(key: Preferences.Key<T>): T? {
        return dataStoreRepository.getFromDataStore(key)
    }

}