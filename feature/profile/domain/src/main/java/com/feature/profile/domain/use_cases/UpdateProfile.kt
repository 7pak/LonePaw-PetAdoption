package com.feature.profile.domain.use_cases

import android.util.Log
import com.core.common.utls.Resource
import com.core.network.DataResponse
import com.core.network.profile_api.model.UpdateProfileData
import com.feature.profile.domain.repository.ProfileRepository
import com.google.gson.Gson
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class UpdateProfile @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    operator fun invoke(updateProfileData: UpdateProfileData): Flow<Resource<HttpResponse>> {
        return flow {
            emit(Resource.Loading(isLoading = true))
            val response = profileRepository.updateProfile(updateProfileData)
            val parsedResponseBody =
                Gson().fromJson(response.bodyAsText(), DataResponse::class.java)
            when (response.status.value) {
                in 200..299 -> {
                    emit(Resource.Success(response,message = parsedResponseBody.message))
                }

                else -> {
                    Log.d("AppError", "updateProfile:${parsedResponseBody.message} ")
                    emit(Resource.Error("An unexpected error occurred: ${parsedResponseBody.message}"))
                }
            }
        }.onCompletion {
            emit(Resource.Loading(isLoading = false))
        }.flowOn(Dispatchers.IO)

    }
}