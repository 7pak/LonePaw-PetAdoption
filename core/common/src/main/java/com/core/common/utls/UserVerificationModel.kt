package com.core.common.utls

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.app_data.AppDataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject


open class UserVerificationModel @Inject constructor(
    private val dataStore: AppDataStoreManager,
) : ViewModel() {


     val tokenFlow: Flow<String?> = dataStore.currentToken
        .flowOn(Dispatchers.Main)
        .buffer(Channel.CONFLATED)


     fun saveToken(token: String) {
        viewModelScope.launch {
            dataStore.saveToken(token)
        }
    }

    fun clearToken() {
        viewModelScope.launch {
            dataStore.clearToken()
        }
    }

    val userIdFlow: Flow<Int?> = dataStore.currentUserId
        .flowOn(Dispatchers.Main)
        .buffer(Channel.CONFLATED)


    fun saveUserId(userId: Int) {
        viewModelScope.launch {
            dataStore.saveUserId(userId)
        }
    }

    fun clearUserId() {
        viewModelScope.launch {
            dataStore.clearUserId()
        }
    }
}