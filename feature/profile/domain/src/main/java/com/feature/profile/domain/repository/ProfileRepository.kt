package com.feature.profile.domain.repository

import com.core.network.DataResponse
import com.core.network.home_api.model.GetPetDataResponse
import com.core.network.home_api.model.PetData
import com.core.network.profile_api.model.AddPostData
import com.core.network.profile_api.model.GetProfileDataResponse
import com.core.network.profile_api.model.UpdateProfileData
import io.ktor.client.statement.HttpResponse


interface ProfileRepository {

    suspend fun getMyPets(): GetPetDataResponse

    suspend fun getPet(id:Int):DataResponse<PetData>

    suspend fun addPost(addPostData: AddPostData): HttpResponse

    suspend fun updatePost(id: Int,addPostData: AddPostData): HttpResponse

    suspend fun getFavorite(): GetPetDataResponse

    suspend fun removeFavorite(id:Int): DataResponse<*>

    suspend fun deletePost(id:Int): DataResponse<*>

    suspend fun getProfile(): GetProfileDataResponse
    suspend fun updateProfile(updateProfileData: UpdateProfileData): HttpResponse
}