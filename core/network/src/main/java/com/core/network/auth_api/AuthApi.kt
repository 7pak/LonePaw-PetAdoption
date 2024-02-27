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
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("api/v1/register")
    suspend fun registerUser(
        @Body registerUserData: RegisterUserData
    ): RegisterUserResponse


    @POST("api/v1/login")
    suspend fun loginUser(
        @Body loginUserData: LoginUserData
    ): LoginUserResponse

    @POST("api/v1/password/verification")
    suspend fun emailVerification(
        @Body emailVerificationData: EmailVerificationData
    ): EmailVerificationResponse

    @POST("api/v1/otp/verify")
    suspend fun verifyOtp(
        @Header("Authorization") token: String,
        @Body otpCode: OtpVerificationData
    ): DataResponse<*>

    @POST("api/v1/otp/resend")
    suspend fun resendOtp(
        @Header("Authorization") token: String,
    ): DataResponse<*>

    @POST("api/v1/password/reset")
    suspend fun resetPassword(
        @Header("Authorization") token: String,
        @Body newPassword: PasswordResetData
    ): DataResponse<*>


}