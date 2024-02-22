package com.core.network.chat_api.models

import com.core.network.DataResponse
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class Message(
    @SerializedName("message")
    val message:String,
    @SerializedName("receiver_id")
    val receiverId:String
)

typealias SendMessageResponse = DataResponse<Message>
