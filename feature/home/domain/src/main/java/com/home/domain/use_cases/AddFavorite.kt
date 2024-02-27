package com.home.domain.use_cases

import android.util.Log
import com.core.common.utls.Resource
import com.core.network.DataResponse
import com.home.domain.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class AddFavorite @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(id:Int): Flow<Resource<DataResponse<*>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            val response = homeRepository.addFavorite(id)
            emit(Resource.Success(response))
        }.catch {

            Log.d("AppError", "addFavorite:${it} ")
            emit(Resource.Error("An unexpected error occurred: ${it.localizedMessage}"))
        }.onCompletion {
            emit(Resource.Loading(isLoading = false))
        }.flowOn(Dispatchers.IO)

    }
}