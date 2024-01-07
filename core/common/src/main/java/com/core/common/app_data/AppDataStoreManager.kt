package com.core.common.app_data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class AppDataStoreManager  @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private object DataStoreKeys {
        const val USER_TOKEN = "USER_TOKEN"
        val userToken = stringPreferencesKey(USER_TOKEN)
    }

    suspend fun saveToken(token:String){
        dataStore.edit {preferences ->
            preferences[DataStoreKeys.userToken] = token
        }
    }

    val currentToken: Flow<String?> = dataStore.data.catch { exception->
        if (exception is IOException){
            Log.d("DataStore", "Exception: ${exception.message.toString()} ")
            emit(emptyPreferences())
        }else{
            Log.d("DataStore", "Exception2: ${exception.message.toString()} ")
            throw exception

        }
    }.map {preferences ->
        val token = preferences[DataStoreKeys.userToken] ?: ""
        token
    }

    suspend fun clearToken(){
        dataStore.edit {preferences ->
            preferences.remove(DataStoreKeys.userToken)
        }
    }
}