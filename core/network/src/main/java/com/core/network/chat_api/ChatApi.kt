package com.core.network.chat_api

import com.core.network.chat_api.models.Message
import com.core.network.chat_api.models.SendMessageResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ChatApi {

    @POST("api/v1/chat/send")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body message: Message
    ):SendMessageResponse
}