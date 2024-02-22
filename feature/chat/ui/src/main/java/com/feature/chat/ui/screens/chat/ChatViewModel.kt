package com.feature.chat.ui.screens.chat

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.utls.Resource
import com.core.common.utls.UserVerificationModel
import com.feature.chat.domain.model.ChatContent
import com.feature.chat.domain.model.MessageStatus
import com.feature.chat.domain.use_cases.ChatUseCase
import com.feature.chat.ui.screens.navArgs
import com.feature.chat.ui.shared.ChatNavArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase,
    private val userVerificationModel: UserVerificationModel,
    handle: SavedStateHandle
) : ViewModel() {


    private var _messagesListState: MutableStateFlow<List<ChatContent>> =
        MutableStateFlow(emptyList())
    val messagesListState: StateFlow<List<ChatContent>> = _messagesListState

    private var _recipientProfilePic: MutableStateFlow<String> =
        MutableStateFlow("")
    val recipientProfilePic: StateFlow<String> = _recipientProfilePic

    private var _recipientName: MutableStateFlow<String> =
        MutableStateFlow("")
    val recipientName: StateFlow<String> = _recipientName

    var state by mutableStateOf(MessageStatus())
        private set

    fun updateState(newState: com.feature.chat.ui.screens.chat.MessageStatus) {
        state = newState
    }

    var currentUserId by mutableIntStateOf(-1)
        private set
    private var recipientId by mutableIntStateOf(-1)


    init {
        viewModelScope.launch {

            currentUserId = userVerificationModel.userIdFlow.firstOrNull() ?: -1

            recipientId = handle.navArgs<ChatNavArgs>().id

            getRecipientProfile()
            getMessages()
        }
    }

    fun sendMessages() {
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        val dateObject = dateFormat.parse(formattedDate)

        viewModelScope.launch {
            chatUseCase.sendMessage(
                chatContent = ChatContent(
                    senderId = currentUserId.toString(),
                    recipientId = recipientId.toString(),
                    date = dateObject,
                    message = state.message,
                    type = state.type,
                    messageStatus = MessageStatus.UNSEEN
                )
            ).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        updateState(MessageStatus())
                    }

                    is Resource.Error -> {
                        Log.d("AppError", "sendMessages: ${resource.message}")
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun getMessages() {
        viewModelScope.launch {
            chatUseCase.getMessages(
                currentUser = currentUserId.toString(),
                recipientId = recipientId.toString()
            ).collectLatest { resource ->

                when (resource) {
                    is Resource.Success -> {
                        resource.data?.let { messages ->
                            _messagesListState.value = messages
                        }
                    }

                    is Resource.Error -> {
                        Log.d("AppError", "getMessage: ${resource.message}")

                    }

                    else -> Unit
                }

            }
        }
    }

    private fun getRecipientProfile() {
        viewModelScope.launch {
            chatUseCase.getRecipientProfile(
                recipientId = recipientId
            ).collectLatest { resource ->

                when (resource) {
                    is Resource.Success -> {
                        resource.data?.let { profile ->
                            _recipientProfilePic.value = profile.profilePic
                            _recipientName.value = profile.name
                        }
                    }

                    is Resource.Error -> {
                        Log.d("AppError", "getRecipientProfile: ${resource.message}")

                    }

                    else -> Unit
                }

            }
        }
    }

}