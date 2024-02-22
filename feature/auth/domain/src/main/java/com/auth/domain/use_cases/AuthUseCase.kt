package com.auth.domain.use_cases

import com.auth.domain.repository.AuthRepository
import com.core.common.utls.Resource
import com.core.network.auth_api.models.LoginUserData
import com.core.network.auth_api.models.LoginUserResponse
import com.core.network.auth_api.models.RegisterUserData
import com.core.network.auth_api.models.RegisterUserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

open class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    open operator fun invoke(registerUserData: RegisterUserData): Flow<Resource<RegisterUserResponse>> {
        return flow {
            emit(Resource.Loading(isLoading = true))
            val response = authRepository.registerUser(registerUserData)
            if (response.isSuccessful) {
                emit(Resource.Success(response))
            } else {
                emit(Resource.Error("Error while singing up: ${response.errorBody()?.string()}"))
            }
        }.onCompletion {
            emit(Resource.Loading(isLoading = false))
        }.flowOn(Dispatchers.IO)
    }

    open operator fun invoke(loginUserData: LoginUserData): Flow<Resource<LoginUserResponse>> {
        return flow {
            emit(Resource.Loading(isLoading = true))
            val response = authRepository.loginUser(loginUserData)
            emit(Resource.Success(response))
        }.catch {
            emit(Resource.Error(it.message ?: "An unexpected error occurred"))
        }.onCompletion {
            emit(Resource.Loading(isLoading = false))
        }.flowOn(Dispatchers.IO)
    }
}