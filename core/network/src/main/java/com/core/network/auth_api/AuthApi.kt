package com.core.network.auth_api

import com.core.network.auth_api.models.LoginUserData
import com.core.network.auth_api.models.LoginUserResponse
import com.core.network.auth_api.models.RegisterUserData
import com.core.network.auth_api.models.RegisterUserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/v1/register")
    suspend fun registerUser(
        @Body registerUserData:RegisterUserData
    ):RegisterUserResponse


    @POST("api/v1/login")
    suspend fun loginUser(
        @Body loginUserData: LoginUserData
    ):LoginUserResponse
}