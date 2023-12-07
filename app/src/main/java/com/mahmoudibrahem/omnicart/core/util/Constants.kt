package com.mahmoudibrahem.omnicart.core.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {

    //App Constants
    const val TAG = "3wiida"
    const val APP_DATA_STORE_NAME = "OMNI_CART_DATA_STORE"
    const val IMAGE_URL = "https://omnicart.onrender.com/images/"

    //Data Store Keys
    val IS_LOGGED_IN_KEY = booleanPreferencesKey("IS_LOGGED_IN_KEY")
    val USER_TOKEN_KEY = stringPreferencesKey("USER_TOKEN_KEY")

    //Runtime Util Constants
    var isLoggedIn = false
    var userToken = ""

}