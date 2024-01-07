package com.core.network.home_api

import com.core.network.DataResponse
import com.core.network.home_api.model.GetPetDataResponse
import com.core.network.home_api.model.PetData
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApi {

    @GET("api/v1/posts")
    suspend fun getPets(
        @Header("Authorization") token: String
    ): GetPetDataResponse

    @GET("api/v1/posts/{id}")
    suspend fun getPet(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): DataResponse<PetData>

    @POST("api/v1/posts/{id}/favorite")
    suspend fun addFavorite(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): DataResponse<*>

    @GET("api/v1/search")
    suspend fun searchResult(
        @Header("Authorization") token: String,
        @Query("text") query: String)
    : GetPetDataResponse

    @GET("api/v1/categories/{id}/posts")
    suspend fun getPostsCategories(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): GetPetDataResponse
}