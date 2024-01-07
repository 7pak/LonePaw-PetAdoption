package com.auth.domain.use_cases

import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import com.auth.domain.repository.AuthRepository
import com.core.common.Resource
import com.core.common.UserVerificationModel
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
    open operator fun invoke(registerUserData: RegisterUserData):Flow<Resource<RegisterUserResponse>>
    {
        return flow {
            emit(Resource.Loading(isLoading = true))
            val response = authRepository.registerUser(registerUserData)
            emit(Resource.Success(response))
        }.catch {
            Log.d("AppError", "signup: ${it.message}--${it.localizedMessage}--${it.cause}--$ ")
            emit(Resource.Error(it.message?: "An unexpected error occurred"))
        }.onCompletion {
            emit(Resource.Loading(isLoading = false))
        }.flowOn(Dispatchers.IO)
    }

    open operator fun invoke(loginUserData: LoginUserData):Flow<Resource<LoginUserResponse>>
    {
        return flow {
            emit(Resource.Loading(isLoading = true))
            val response = authRepository.loginUser(loginUserData)
            emit(Resource.Success(response))
        }.catch {
            emit(Resource.Error(it.message?: "An unexpected error occurred"))
        }.onCompletion {
            emit(Resource.Loading(isLoading = false))
        }.flowOn(Dispatchers.IO)
    }
}