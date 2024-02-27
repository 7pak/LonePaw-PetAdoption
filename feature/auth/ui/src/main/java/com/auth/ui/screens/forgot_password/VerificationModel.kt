package com.auth.ui.screens.forgot_password

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth.domain.use_cases.AuthUseCase
import com.core.common.utls.Resource
import com.core.common.utls.UserVerificationModel
import com.core.network.auth_api.models.EmailVerificationData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val userVerificationModel: UserVerificationModel
) : ViewModel() {

    var state by mutableStateOf(VerificationStates())
        private set

    private var _otpHasSent: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val otpHasSent: StateFlow<Boolean> = _otpHasSent

    private var _otpIsCorrect: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val otpIsCorrect: StateFlow<Boolean> = _otpIsCorrect

    private var _passwordHasReset: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val passwordHasReset: StateFlow<Boolean> = _passwordHasReset

    private val initiateValue = 5 * 60 * 1000L
    private var _remainingTime: MutableStateFlow<Long> = MutableStateFlow(initiateValue)
    val remainingTime: StateFlow<Long> = _remainingTime


    fun updateState(newStates: VerificationStates) {
        state = newStates
    }

    fun updateRemainingTime(newTime: Long) {
        _remainingTime.value = newTime
    }

    fun verifyEmail() {
        viewModelScope.launch {
            authUseCase.verifyEmail(EmailVerificationData(email = state.email))
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            state = state.copy(isLoading = resource.isLoading)
                        }

                        is Resource.Success -> {
                            val token = resource.data?.data?.token
                            token?.let {
                                userVerificationModel.saveToken(it)
                            }

                            _otpHasSent.value = true

                        }

                        is Resource.Error -> {
                            Log.d("AppError", "verifyEmail: ${resource.message}")
                            state = state.copy(error = resource.message)
                        }
                    }
                }
        }
    }

    fun verifyOtp() {
        viewModelScope.launch {
            authUseCase.verifyOtp(state.otp).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        state = state.copy(isLoading = resource.isLoading)
                    }

                    is Resource.Success -> {
                        _otpIsCorrect.value = true
                    }

                    is Resource.Error -> {
                        Log.d("AppError", "verifyOtp: ${resource.message}")
                        state = state.copy(error = "Otp Is not correct: "+resource.message)
                    }
                }
            }
        }
    }

    fun resendOtp() {
        viewModelScope.launch {
            authUseCase.resendOtp().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        state = state.copy(isLoading = resource.isLoading)
                    }

                    is Resource.Success -> {
                        Log.d("AppSuccess", "resendOtp: successfully ")
                        _remainingTime.value = initiateValue
                    }

                    is Resource.Error -> {
                        Log.d("AppError", "verifyOtp: ${resource.message}")
                        state = state.copy(error = "Error while resending otp check your internet: "+resource.message)
                    }
                }
            }
        }
    }

    fun resetPassword() {
        viewModelScope.launch {
            authUseCase.resetPassword(state.newPassword).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        state = state.copy(isLoading = resource.isLoading)
                    }

                    is Resource.Success -> {
                        Log.d("AppSuccess", "resetPassword: successfully")
                        _passwordHasReset.value = true
                    }

                    is Resource.Error -> {
                        Log.d("AppError", "resetPassword: ${resource.message}")
                        state = state.copy(error = "Error While resetting password, please enter valid password: "+resource.message)
                    }
                }
            }
        }
    }

    fun cancel(){
        userVerificationModel.clearToken()
    }
}