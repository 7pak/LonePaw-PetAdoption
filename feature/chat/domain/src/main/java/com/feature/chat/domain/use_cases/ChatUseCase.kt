package com.feature.chat.domain.use_cases

data class ChatUseCase(
    val sendMessage:SendMessage,
    val getMessages: GetMessages,
    val getContacts: GetContacts,
    val searchContact: SearchContact,
    val deleteChat: DeleteChat,
    val getRecipientProfile: GetRecipientProfile,
)
