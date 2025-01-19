package com.abir.androidbasicpart1.datalayer.localStorage.dataStore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


// Define DataStore and Preference keys
val Context.dataStore by preferencesDataStore("user_prefs")
val LOGIN_STATUS_KEY = booleanPreferencesKey("is_logged_in")
val USERNAME_KEY = stringPreferencesKey("username")

// Functions to save and retrieve data
suspend fun saveLoginState(context: Context, isLoggedIn: Boolean, username: String) {
    context.dataStore.edit { preferences ->
        preferences[LOGIN_STATUS_KEY] = isLoggedIn
        preferences[USERNAME_KEY] = username
    }
}

fun getLoginState(context: Context): Flow<Boolean> {
    return context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[LOGIN_STATUS_KEY] ?: false
        }
}

fun getUsername(context: Context): Flow<String> {
    return context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
        }
        .map { preferences ->
            preferences[USERNAME_KEY] ?: ""
        }
}