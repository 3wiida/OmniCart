package com.mahmoudibrahem.omnicart

import android.app.Application
import android.util.Log
import com.mahmoudibrahem.omnicart.core.util.Constants
import com.mahmoudibrahem.omnicart.core.util.Constants.IS_LOGGED_IN_KEY
import com.mahmoudibrahem.omnicart.core.util.Constants.TAG
import com.mahmoudibrahem.omnicart.core.util.Constants.USER_TOKEN_KEY
import com.mahmoudibrahem.omnicart.domain.usecase.GetFromDataStoreUseCase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class OmniCartApplication : Application() {

    @Inject
    lateinit var getFromDataStoreUseCase: GetFromDataStoreUseCase

    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()
        job = CoroutineScope(Dispatchers.IO).launch {
            val isLoggedIn = getFromDataStoreUseCase(key = IS_LOGGED_IN_KEY) ?: false
            val userToken = getFromDataStoreUseCase(key = USER_TOKEN_KEY) ?: ""
            Constants.isLoggedIn = isLoggedIn
            Constants.userToken = userToken
            Log.d(TAG, "onCreate: ${Constants.userToken}")
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        job?.cancel()
    }

    override fun onTerminate() {
        super.onTerminate()
        job?.cancel()
    }
}