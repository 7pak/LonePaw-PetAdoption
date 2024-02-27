package com.core.network.profile_api

import com.core.network.DataResponse
import com.core.network.home_api.model.GetPetDataResponse
import com.core.network.profile_api.model.GetProfileDataResponse
import com.core.network.profile_api.model.UpdatePasswordData
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileApi {

    @GET("api/v1/user/posts")
    suspend fun getMyPets(
        @Header("Authorization") token: String
    ): GetPetDataResponse


    @GET("api/v1/favorites")
    suspend fun getFavorites(
        @Header("Authorization") token: String
    ): GetPetDataResponse


    @DELETE("api/v1/posts/{id}/favorite")
    suspend fun removeFavorite(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): DataResponse<*>

    @GET("api/v1/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): GetProfileDataResponse

    @DELETE("api/v1/posts/{id}")
    suspend fun deletePost(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): DataResponse<*>

    @PUT("api/v1/profile/update/password")
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Body passwordData: UpdatePasswordData
    ): DataResponse<*>


}