package com.auth.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.domain.use_cases.AuthUseCase
import com.core.common.Resource
import com.core.common.UserVerificationModel
import com.core.network.auth_api.models.RegisterUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpModel @Inject constructor(private val authUseCase: AuthUseCase,private val userVerificationModel: UserVerificationModel) : ViewModel() {

    private val _state: MutableStateFlow<RegisterStates> = MutableStateFlow(RegisterStates())
    val state: StateFlow<RegisterStates> = _state

    fun registerUser() {

        viewModelScope.launch {
            authUseCase(
                RegisterUserData(
                    name = state.value.fullName,
                    username = state.value.username,
                    email = state.value.email,
                    password = state.value.password,
                    country = state.value.country,
                    contactNumber = state.value.contactNumber,
                    address = state.value.address
                )
            )
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            updateStatus(
                                _state.value.copy(
                                    success = resource.data?.message,
                                    token = resource.data?.data?.token
                                )
                            )
                            state.value.token?.let {
                                userVerificationModel.saveToken(it)
                            }

                        }

                        is Resource.Loading -> updateStatus(_state.value.copy(isLoading = resource.isLoading))
                        is Resource.Error -> updateStatus(_state.value.copy(error = resource.message))
                    }
                }
        }

    }

    fun updateStatus(status: RegisterStates) {
        _state.value = status.copy()
    }
}