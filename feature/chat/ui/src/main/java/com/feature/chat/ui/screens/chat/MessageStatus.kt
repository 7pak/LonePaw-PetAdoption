package com.feature.chat.ui.screens.chat

import com.feature.chat.domain.model.TextMessage

data class MessageStatus(
    val message:String = "",
    val type: String = TextMessage.TEXT
)
