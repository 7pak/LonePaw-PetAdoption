package com.home.domain.use_cases

import android.util.Log
import com.core.common.Resource
import com.core.database.model.PetInfo
import com.home.domain.HomeRepositroy
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

class GetPet @Inject constructor(
    private val homeRepository: HomeRepositroy,
    private val petsDao: com.core.database.dao.PetsDao
) {
    operator fun invoke(id:Int): Flow<Resource<PetInfo>> {
        var retryCount = 0
        return flow {
            emit(Resource.Loading(isLoading = true))

//            val response = homeRepository.getPet(id)
//            val remotePost =response.data
//            if (post.status==200 || post.status == 202){
//                Log.d("AppSuccess", "invoke:${post.data} ")
//            }
            val localPost = petsDao.getPetById(id)
            //     if (response.data == null || response.message.isNullOrEmpty()) {
            emit(Resource.Success(data = localPost))
//            }else
//                emit(Resource.Success(data = remotePost?.toPetInfo()))
//            }


        }.catch { cause ->


            if (cause is HttpException && cause.code() == 429) {
                // Retry after a delay, with exponential backoff
                delay(2_000 * (2.toDouble().pow(retryCount.toDouble()).toLong()))
                emit(Resource.Loading(isLoading = true))
                retryCount++
                return@catch
            }
            Log.d("AppError", "invoke:${cause} ")

            emit(Resource.Error("An unexpected error occurred: ${cause.localizedMessage}"))
        }
            .onCompletion {
                emit(Resource.Loading(isLoading = false))
            }.flowOn(Dispatchers.IO)
    }
}