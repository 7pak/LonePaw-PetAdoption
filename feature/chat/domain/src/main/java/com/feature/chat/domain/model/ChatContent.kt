package com.feature.chat.domain.model

import java.util.Date

data class ChatContent(
    val chatId:String = "",
    val date:Date? = null,
    val message:String = "",
    val recipientId:String = "",
    val recipientName:String = "",
    val senderId:String = "",
    val senderName:String = "",
    val type: String = TextMessage.TEXT,
    val messageStatus:String = MessageStatus.UNSEEN
)

interface MessageType

object TextMessage:MessageType{
    const val TEXT = "TEXT"
}

object ImageMessage:MessageType{
    const val IMAGE = "IMAGE"
}

object MessageStatus{
    const val SEEN = "SEEN"

    const val UNSEEN = "UNSEEN"
}
