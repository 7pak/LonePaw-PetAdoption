package com.home.data.repository

import com.core.network.DataResponse
import com.core.network.home_api.HomeDataProvider
import com.core.network.home_api.model.GetPetDataResponse
import com.core.network.home_api.model.PetData
import com.core.network.profile_api.model.GetProfileDataResponse
import com.home.domain.repository.HomeRepositroy

class HomeRepositoryImpl(
    private val homeDataProvider: HomeDataProvider
): HomeRepositroy {
    override suspend fun getPets(): GetPetDataResponse {
        return homeDataProvider.getPets()
    }

    override suspend fun getPet(id: Int): DataResponse<PetData> {
        return homeDataProvider.getPet(id)
    }

    override suspend fun searchResult(query:String):GetPetDataResponse {
        return homeDataProvider.searchResult(query = query)
    }

    override suspend fun getPostCategories(id: Int):GetPetDataResponse {
        return homeDataProvider.getPostsCategories( id = id)
    }

    override suspend fun addFavorite(id: Int): DataResponse<*> {
        return homeDataProvider.addFavorite(id)
    }

    override suspend fun removeFavorite(id:Int): DataResponse<*> {
        return homeDataProvider.removeFavorite(id)
    }

    override suspend fun getProfile(): GetProfileDataResponse {
        return homeDataProvider.getProfile()
    }

}