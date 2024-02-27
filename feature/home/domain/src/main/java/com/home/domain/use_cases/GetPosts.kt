package com.home.domain.use_cases

import com.core.common.utls.Resource
import com.core.database.dao.PetsDao
import com.core.database.model.toPetInfo
import com.core.network.home_api.model.GetPetDataResponse
import com.home.domain.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow


@Singleton
open class GetPosts @Inject constructor(
    private val homeRepository: HomeRepository,
    private val petsDao: PetsDao
) {

    open operator fun invoke(): Flow<Resource<GetPetDataResponse>> {

        var retryCount = 0
        return flow {
            emit(Resource.Loading(isLoading = true))

            val remotePosts =homeRepository.getPets()

            remotePosts.data?.let { list->
                petsDao.deleteAll()
                petsDao.upsertPets(petInfo = list.map {
                    it.toPetInfo()
                })
            }

            emit(Resource.Success(data = remotePosts))

        }.catch {cause ->


            if (cause is HttpException && cause.code() == 429) {
                // Retry after a delay, with exponential backoff
                delay(2_000 * (2.toDouble().pow(retryCount.toDouble()).toLong()))
                emit(Resource.Loading(isLoading = true))
                retryCount++
                return@catch
            }

            emit(Resource.Error("An unexpected error occurred: ${cause.localizedMessage}"))
        }
            .onCompletion {
            emit(Resource.Loading(isLoading = false))
        }.flowOn(Dispatchers.IO)
    }
}