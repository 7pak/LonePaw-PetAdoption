package com.home.domain.use_cases

import com.core.common.Resource
import com.core.database.model.toPetInfo
import com.core.network.home_api.model.GetPetDataResponse
import com.home.domain.HomeRepositroy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow


@Singleton
class GetPosts @Inject constructor(
    private val homeRepository: HomeRepositroy,
    private val petsDao: com.core.database.dao.PetsDao
) {

    operator fun invoke(): Flow<Resource<GetPetDataResponse>> {
        var retryCount = 0
       var lastRequestTime = 0L
        return flow {
            emit(Resource.Loading(isLoading = true))

            val remotePosts =homeRepository.getPets()

            remotePosts.data?.let { list->
                petsDao.deleteAll()
                petsDao.upsertPet(petInfo = list.map {
                    it.toPetInfo()
                })
            }

            val localPosts = petsDao.getAllPets()

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
//            .onStart {
//            val currentTime = System.currentTimeMillis()
//            val timeSinceLastRequest = currentTime - lastRequestTime
//            val minTimeBetweenRequests = 2000L  // Adjust this value based on your needs
//
//            if (timeSinceLastRequest < minTimeBetweenRequests) {
//                delay(minTimeBetweenRequests - timeSinceLastRequest)
//            }
//
//            lastRequestTime = System.currentTimeMillis()
//        }
            .onCompletion {
            emit(Resource.Loading(isLoading = false))
        }.flowOn(Dispatchers.IO)
    }
}