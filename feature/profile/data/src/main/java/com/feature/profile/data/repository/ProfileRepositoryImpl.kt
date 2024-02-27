package com.feature.profile.data.repository

import com.core.network.DataResponse
import com.core.network.home_api.model.GetPetDataResponse
import com.core.network.home_api.model.PetData
import com.core.network.profile_api.ProfileDataProvider
import com.core.network.profile_api.model.AddPostData
import com.core.network.profile_api.model.GetProfileDataResponse
import com.core.network.profile_api.model.UpdatePasswordData
import com.core.network.profile_api.model.UpdateProfileData
import com.feature.profile.domain.repository.ProfileRepository
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataProvider: ProfileDataProvider
) : ProfileRepository {
    override suspend fun getMyPets(): GetPetDataResponse {
        return profileDataProvider.getMyPets()
    }

    override suspend fun getPet(id: Int): DataResponse<PetData> {
        return profileDataProvider.getPet(id)
    }

    override suspend fun addPost(addPostData: AddPostData): HttpResponse {
        return profileDataProvider.addPost(addPostData)
    }

    override suspend fun updatePost(id: Int, addPostData: AddPostData): HttpResponse {
        return profileDataProvider.updatePost(id = id,addPostData)
    }

    override suspend fun deletePost(id: Int): DataResponse<*> {
        return profileDataProvider.deletePost(id)
    }

    override suspend fun getFavorite(): GetPetDataResponse {
        return profileDataProvider.getFavorite()
    }

    override suspend fun removeFavorite(id: Int): DataResponse<*> {
        return profileDataProvider.removeFavorite(id)
    }

    override suspend fun getProfile(): GetProfileDataResponse {
        return profileDataProvider.getProfile()
    }

    override suspend fun updateProfile(updateProfileData: UpdateProfileData): HttpResponse {
        return profileDataProvider.updateProfile(updateProfileData)
    }

    override suspend fun updatePassword(
        passwordData:UpdatePasswordData
    ): DataResponse<*>{
        return profileDataProvider.updatePassword(passwordData = passwordData)
    }


}