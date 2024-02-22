package com.home.domain.use_cases

import com.core.common.utls.Resource
import com.core.network.home_api.model.GetPetDataResponse
import com.home.domain.repository.HomeRepositroy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetPostsCategory @Inject constructor(
    private val homeRepository: HomeRepositroy,
) {

    operator fun invoke(id:Int): Flow<Resource<GetPetDataResponse>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            val remotePosts =homeRepository.getPostCategories(id)

            emit(Resource.Success(data = remotePosts))

        }.catch {cause ->
            emit(Resource.Error("An unexpected error occurred: ${cause.localizedMessage}"))
        }
            .onCompletion {
            emit(Resource.Loading(isLoading = false))
        }.flowOn(Dispatchers.IO)
    }
}