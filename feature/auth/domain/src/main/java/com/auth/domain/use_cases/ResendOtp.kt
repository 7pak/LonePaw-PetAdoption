package com.auth.domain.use_cases

import com.auth.domain.repository.AuthRepository
import com.core.common.utls.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class ResendOtp @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() = flow {

        emit(Resource.Loading(isLoading = true))

        val response = authRepository.resendOtp()
        emit(Resource.Success(response))
    }.catch {
        emit(Resource.Error(it.message ?: "An unexpected error occurred"))
    }.onCompletion {
        emit(Resource.Loading(isLoading = false))
    }.flowOn(Dispatchers.IO)
}