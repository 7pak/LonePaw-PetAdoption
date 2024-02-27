package com.feature.profile.domain.use_cases

import com.core.common.utls.Resource
import com.core.network.DataResponse
import com.core.network.profile_api.model.UpdatePasswordData
import com.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import java.io.IOException
import javax.inject.Inject

class UpdatePassword @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke(oldPassword:String,newPassword: String): Flow<Resource<DataResponse<*>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            val response = profileRepository.updatePassword(UpdatePasswordData(oldPassword = oldPassword,newPassword = newPassword))

            emit(Resource.Success(response))

        }.catch { error ->
            if (error is IOException) {
                emit(Resource.Error("No internet connection. Please check your network connection and try again."))

            } else {
                emit(Resource.Error(error.message ?: "An unexpected error occurred"))
            }
        }.onCompletion {
            emit(Resource.Loading(isLoading = false))
        }.flowOn(Dispatchers.IO)
    }
}