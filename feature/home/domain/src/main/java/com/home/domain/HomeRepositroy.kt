package com.home.domain

import com.core.network.DataResponse
import com.core.network.home_api.model.GetPetDataResponse
import com.core.network.home_api.model.PetData
import com.core.network.profile_api.model.GetProfileDataResponse

interface HomeRepositroy {
    suspend fun getPets(): GetPetDataResponse

    suspend fun getPet(id: Int): DataResponse<PetData>

    suspend fun addFavorite(id: Int): DataResponse<*>

    suspend fun removeFavorite(id: Int): DataResponse<*>

    suspend fun getProfile(): GetProfileDataResponse

    suspend fun searchResult(query:String):GetPetDataResponse

    suspend fun getPostCategories(id: Int):GetPetDataResponse
}