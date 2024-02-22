package com.feature.profile.domain.use_cases

import android.util.Log
import com.core.common.utls.Resource
import com.core.database.model.PetInfo
import com.core.database.model.toPetInfo
import com.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.math.pow

class GetFavoritePets @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    operator fun invoke(): Flow<Resource<List<PetInfo>>> {
        var retryCount = 0
        return flow {
                emit(Resource.Loading(isLoading = true))

                val remotePets = profileRepository.getFavorite()

                emit(Resource.Success(remotePets.data?.map { it.toPetInfo() }))

            }.catch {cause ->

                if (cause is HttpException && cause.code() == 429) {
                    // Retry after a delay, with exponential backoff
                    delay(2_000 * (2.toDouble().pow(retryCount.toDouble()).toLong()))
                    emit(Resource.Loading(isLoading = true))
                    retryCount++
                    return@catch
                }
                Log.d("AppError", "getFavorite:${cause} ")

                emit(Resource.Error("An unexpected error occurred: ${cause.localizedMessage}"))
            }
                .onCompletion {
                    emit(Resource.Loading(isLoading = false))
                }.flowOn(Dispatchers.IO)
        }
    }