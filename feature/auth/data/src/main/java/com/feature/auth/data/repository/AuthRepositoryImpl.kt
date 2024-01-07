package com.feature.auth.data.repository

import com.auth.domain.repository.AuthRepository
import com.core.network.auth_api.AuthDataProvider
import com.core.network.auth_api.models.LoginUserData
import com.core.network.auth_api.models.LoginUserResponse
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
}