package com.feature.profile.domain.use_cases

import com.core.common.utls.Resource
import com.core.network.profile_api.model.AddPostData
import com.feature.profile.domain.repository.ProfileRepository
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class AddPost @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    operator fun invoke(addPostData: AddPostData): Flow<Resource<HttpResponse>> {
        return flow {
            emit(Resource.Loading(isLoading = true))


            val response = profileRepository.addPost(addPostData)

            when (response.status.value) {
                 in 200..299-> {
                     emit(Resource.Success(response))
                 }
                in  400..499-> {
                    emit(Resource.Error("An unexpected error occurred: ${response.status.description}"))
                 }
                in 500 ..599->{

                }
            }

        }.onCompletion {
            emit(Resource.Loading(isLoading = false))
        }.flowOn(Dispatchers.IO)

    }
}