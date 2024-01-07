package com.core.network.home_api

import com.core.network.DataResponse
import com.core.network.home_api.model.GetPetDataResponse
import com.core.network.home_api.model.PetData
import com.core.network.profile_api.ProfileApi
import com.core.network.profile_api.model.GetProfileDataResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class HomeDataProvider @Inject constructor(
    private val homeApi: HomeApi,
    private val tokenFlow: Flow<String?>,
    private val profileApi: ProfileApi
    ) {

    private suspend fun getToken(): String? {
        return tokenFlow.firstOrNull()
    }

    suspend fun getPets():GetPetDataResponse {
        return homeApi.getPets(token ="Bearer ${getToken()}")
    }

    suspend fun searchResult(query:String):GetPetDataResponse {
        return homeApi.searchResult(token ="Bearer ${getToken()}",query = query)
    }

    suspend fun getPostsCategories(id: Int):GetPetDataResponse {
        return homeApi.getPostsCategories(token ="Bearer ${getToken()}", id = id)
    }

    suspend fun getPet(id:Int):DataResponse<PetData> {
        return homeApi.getPet(id=id, token = "Bearer ${getToken()}")
    }

    suspend fun addFavorite(id: Int): DataResponse<*> {
        return homeApi.addFavorite("Bearer ${getToken()}",id)
    }

    suspend fun removeFavorite(id: Int): DataResponse<*> {
        return profileApi.removeFavorite("Bearer ${getToken()}", id)
    }

    suspend fun getProfile(): GetProfileDataResponse {
        return profileApi.getProfile("Bearer ${getToken()}")
    }
}