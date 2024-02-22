package com.feature.chat.ui.screens.contacts

import com.feature.chat.domain.model.ChatContent
import com.feature.chat.domain.model.User

data class ContactStatus(
    val users:List<Pair<User, ChatContent>> = emptyList(),
    val isLoading:Boolean = false,
    val error:String = ""
)
