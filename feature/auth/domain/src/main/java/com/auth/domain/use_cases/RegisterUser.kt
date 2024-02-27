package com.auth.domain.use_cases

import com.auth.domain.repository.AuthRepository
import com.core.common.utls.Resource
import com.core.network.DataResponse
import com.core.network.auth_api.models.RegisterUserData
import com.core.network.auth_api.models.RegisterUserResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

open class RegisterUser @Inject constructor(
    private val authRepository: AuthRepository
) {
    open operator fun invoke(registerUserData: RegisterUserData): Flow<Resource<RegisterUserResponse>> {
        return flow {
            emit(Resource.Loading(isLoading = true))
            val response = authRepository.registerUser(registerUserData)
            val parsedResponseBody =
                Gson().fromJson(response.errorBody()?.string(), DataResponse::class.java)
            if (response.isSuccessful) {
                emit(Resource.Success(response))
            } else {
                emit(Resource.Error("Error while singing up: ${parsedResponseBody.message}"))
            }
        }.onCompletion {
            emit(Resource.Loading(isLoading = false))
        }.flowOn(Dispatchers.IO)
    }
}