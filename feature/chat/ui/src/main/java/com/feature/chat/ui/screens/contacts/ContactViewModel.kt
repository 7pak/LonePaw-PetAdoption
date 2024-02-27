package com.feature.chat.ui.screens.contacts

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.common.utls.Resource
import com.core.common.utls.UserVerificationModel
import com.feature.chat.domain.use_cases.ChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContactViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase,
    private val userVerificationModel: UserVerificationModel
) : ViewModel() {

    var state by mutableStateOf(ContactStatus())
        private set

    private var currentUserId by mutableIntStateOf(-1)

    private var _searchQuery:MutableStateFlow<String> = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()


    fun updateSearchQuery(searchQuery:String){
        _searchQuery.value = searchQuery
    }
    init {
        viewModelScope.launch {
            currentUserId = userVerificationModel.userIdFlow.firstOrNull() ?: -1

        }
    }

    fun getContacts() {
        viewModelScope.launch {
            chatUseCase.getContacts(currentUserId).collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> state = state.copy(isLoading = resource.isLoading)
                    is Resource.Success -> {
                        resource.data?.let {
                            state = state.copy(users = it)
                        }
                    }

                    is Resource.Error -> {
                        Log.d("AppError", "getChats:${resource.message} ")
                    }
                }
            }
        }
    }

     fun getSearchedContact(){
        viewModelScope.launch {
            chatUseCase.searchContact(currentUserId.toString(), searchQuery = searchQuery.value).collectLatest {resource->
                when (resource) {
                    is Resource.Loading -> Unit
                    is Resource.Success -> {
                        resource.data?.let {users ->
                            val filteredUsers = state.users.filter { userPair ->
                                users.any { user -> user.name == userPair.first.name }
                            }
                            state = state.copy(users = filteredUsers)
                        }
                    }
                    is Resource.Error -> Unit

                }
            }
        }
    }

    fun deleteChat(recipientId: String) {
        viewModelScope.launch {
            chatUseCase.deleteChat(
                currentUserId = currentUserId.toString(),
                recipientId = recipientId
            ).collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> {
                        Log.d("AppError", "ContactsScreen: deletedUser error ${resource.message}")
                    }

                    is Resource.Loading -> Unit
                    is Resource.Success -> {
                        state =
                            state.copy(users = state.users.filter { it.first.id != recipientId })
                    }
                }
            }
        }
    }
}