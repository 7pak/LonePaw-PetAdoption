package com.feature.profile.domain.use_cases

import android.util.Log
import com.core.common.utls.Resource
import com.core.network.profile_api.model.AddPostData
import com.feature.profile.domain.repository.ProfileRepository
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class UpdatePost @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    operator fun invoke(id:Int,addPostData: AddPostData): Flow<Resource<HttpResponse>> {
        return flow {
            emit(Resource.Loading(isLoading = true))


            val response = profileRepository.updatePost(id = id,addPostData)
            emit(Resource.Success(response))

        }.catch {
            Log.d("AppError", "updatePost:${it} ")
            emit(Resource.Error("An unexpected error occurred: ${it.localizedMessage}"))
        }.onCompletion {
            emit(Resource.Loading(isLoading = false))
        }.flowOn(Dispatchers.IO)

    }
}