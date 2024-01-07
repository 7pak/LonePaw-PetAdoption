package com.auth.domain.repository

import com.core.network.auth_api.models.LoginUserData
import com.core.network.auth_api.models.LoginUserResponse
import com.core.network.auth_api.models.RegisterUserData
import com.core.network.auth_api.models.RegisterUserResponse

interface AuthRepository {
    suspend fun registerUser(registerUserData: RegisterUserData):RegisterUserResponse
    suspend fun loginUser(loginUserData: LoginUserData):LoginUserResponse
}