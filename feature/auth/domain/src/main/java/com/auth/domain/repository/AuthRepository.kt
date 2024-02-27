package com.auth.domain.repository

import com.core.network.DataResponse
import com.core.network.auth_api.models.EmailVerificationData
import com.core.network.auth_api.models.EmailVerificationResponse
import com.core.network.auth_api.models.LoginUserData
import com.core.network.auth_api.models.LoginUserResponse
import com.core.network.auth_api.models.OtpVerificationData
import com.core.network.auth_api.models.PasswordResetData
import com.core.network.auth_api.models.RegisterUserData
import com.core.network.auth_api.models.RegisterUserResponse

interface AuthRepository {
    suspend fun registerUser(registerUserData: RegisterUserData):RegisterUserResponse
    suspend fun loginUser(loginUserData: LoginUserData):LoginUserResponse

    suspend fun emailVerification(emailVerificationData: EmailVerificationData):EmailVerificationResponse


    suspend fun verifyOtp(otp: OtpVerificationData): DataResponse<*>

    suspend fun resendOtp(): DataResponse<*>

    suspend fun resetPassword(newPassword: PasswordResetData): DataResponse<*>
}