package com.core.network.auth_api

import com.core.network.DataResponse
import com.core.network.auth_api.models.LoginUserData
import com.core.network.auth_api.models.LoginUserResponse
import com.core.network.auth_api.models.RegisterUserData
import com.core.network.auth_api.models.RegisterUserResponse
import com.core.network.auth_api.models.EmailVerificationData
import com.core.network.auth_api.models.EmailVerificationResponse
import com.core.network.auth_api.models.OtpVerificationData
import com.core.network.auth_api.models.PasswordResetData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthDataProvider @Inject constructor(
    private val authApi: AuthApi,
    private val tokenFlow: Flow<String?>
) {

    private suspend fun getToken(): String? {
        return tokenFlow.firstOrNull()
    }

    suspend fun registerUser(registerUserData: RegisterUserData): RegisterUserResponse {
        return authApi.registerUser(registerUserData)
    }

    suspend fun loginUser(loginUserData: LoginUserData): LoginUserResponse {
        return authApi.loginUser(loginUserData)
    }


    suspend fun emailVerification(emailVerificationData: EmailVerificationData):EmailVerificationResponse {
        return authApi.emailVerification(emailVerificationData = emailVerificationData)
    }


    suspend fun verifyOtp(otp: OtpVerificationData): DataResponse<*> {
        return authApi.verifyOtp(token = "Bearer ${getToken()}", otpCode = otp)
    }

    suspend fun resendOtp(): DataResponse<*> {
        return authApi.resendOtp(token = "Bearer ${getToken()}")
    }

    suspend fun resetPassword(newPassword: PasswordResetData): DataResponse<*> {
        return authApi.resetPassword(token = "Bearer ${getToken()}", newPassword = newPassword)
    }
}