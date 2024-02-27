package com.auth.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.domain.use_cases.AuthUseCase
import com.core.common.utls.Resource
import com.core.common.utls.UserVerificationModel
import com.core.network.auth_api.models.LoginUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userVerificationModel: UserVerificationModel
) : ViewModel() {
    private val _state: MutableStateFlow<LoginStates> = MutableStateFlow(LoginStates())
    val state: StateFlow<LoginStates> = _state

    fun loginUser() {

        viewModelScope.launch {
            authUseCase.loginUser(
                LoginUserData(
                    username_email = state.value.emailOrUsername,
                    password = state.value.password
                )
            )
                .collectLatest { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val token = resource.data?.data?.token
                            val userId = resource.data?.data?.userId
                            updateStatus(
                                _state.value.copy(
                                    success = resource.data?.message
                                )
                            )
                            token?.let {
                                userVerificationModel.saveToken(it)
                            }
                            userId?.let {
                                userVerificationModel.saveUserId(it)
                            }
                        }

                        is Resource.Loading -> updateStatus(_state.value.copy(isLoading = resource.isLoading))
                        is Resource.Error -> updateStatus(_state.value.copy(error = resource.message + ": ${resource.data?.message}"))
                    }
                }
        }

    }

    fun updateStatus(status: LoginStates) {
        _state.value = status.copy()
    }
}
