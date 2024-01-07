package com.core.network.auth_api

import com.core.network.auth_api.models.LoginUserData
import com.core.network.auth_api.models.LoginUserResponse
import com.core.network.auth_api.models.RegisterUserData
import com.core.network.auth_api.models.RegisterUserResponse
import javax.inject.Inject

class AuthDataProvider @Inject constructor(
    private val authApi: AuthApi
) {
    suspend fun registerUser(registerUserData:RegisterUserData):RegisterUserResponse{
        return authApi.registerUser(registerUserData)
    }

    suspend fun loginUser(loginUserData: LoginUserData):LoginUserResponse{
        return authApi.loginUser(loginUserData)
    }
}