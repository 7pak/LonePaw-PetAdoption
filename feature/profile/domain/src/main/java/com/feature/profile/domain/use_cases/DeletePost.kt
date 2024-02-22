package com.feature.profile.domain.use_cases

import android.util.Log
import com.core.common.utls.Resource
import com.core.network.DataResponse
import com.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class DeletePost @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(id:Int): Flow<Resource<DataResponse<*>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            val response = profileRepository.deletePost(id)
            emit(Resource.Success(response))
        }.catch {

            Log.d("AppError", "deletePost:${it} ")
            emit(Resource.Error("An unexpected error occurred: ${it.localizedMessage}"))
        }.onCompletion {
            emit(Resource.Loading(isLoading = false))
        }.flowOn(Dispatchers.IO)

    }
}