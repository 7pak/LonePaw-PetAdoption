package com.feature.auth.data.repository

import com.auth.domain.repository.AuthRepository
import com.core.network.DataResponse
import com.core.network.auth_api.AuthDataProvider
import com.core.network.auth_api.models.EmailVerificationData
import com.core.network.auth_api.models.EmailVerificationResponse
import com.core.network.auth_api.models.LoginUserData
import com.core.network.auth_api.models.LoginUserResponse
import com.core.network.auth_api.models.OtpVerificationData
import com.core.network.auth_api.models.PasswordResetData
import com.core.network.auth_api.models.RegisterUserData
import com.core.network.auth_api.models.RegisterUserResponse
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authDataProvider: AuthDataProvider):AuthRepository {
    override suspend fun registerUser(registerUserData: RegisterUserData): RegisterUserResponse {
        return authDataProvider.registerUser(registerUserData)
    }
    override suspend fun loginUser(loginUserData: LoginUserData): LoginUserResponse {
        return authDataProvider.loginUser(loginUserData)
    }

    override suspend fun emailVerification(emailVerificationData: EmailVerificationData): EmailVerificationResponse {
        return authDataProvider.emailVerification(emailVerificationData)
    }

    override suspend fun verifyOtp(otp: OtpVerificationData): DataResponse<*> {
        return authDataProvider.verifyOtp(otp)
    }

    override suspend fun resendOtp(): DataResponse<*> {
        return authDataProvider.resendOtp()
    }

    override suspend fun resetPassword(newPassword: PasswordResetData): DataResponse<*> {
        return authDataProvider.resetPassword(newPassword)
    }
}