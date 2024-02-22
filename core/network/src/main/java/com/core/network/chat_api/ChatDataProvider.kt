package com.core.network.chat_api

import com.core.network.chat_api.models.Message
import com.core.network.chat_api.models.SendMessageResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class ChatDataProvider @Inject constructor(
    private val chatApi: ChatApi,
    private val tokenFlow: Flow<String?>
    ) {

    private suspend fun getToken(): String? {
        return tokenFlow.firstOrNull()
    }

    suspend fun sendMessage(message: Message):SendMessageResponse{
        return chatApi.sendMessage(token = "Bearer ${getToken()}",message)
    }
}