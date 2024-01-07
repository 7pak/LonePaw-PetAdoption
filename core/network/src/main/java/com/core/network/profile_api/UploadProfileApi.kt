package com.core.network.profile_api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.statement.HttpResponse

interface UploadProfileApi {


    @POST("api/v1/posts")
    suspend fun addPost(
        @Header("Authorization") token: String,
        @Body map: MultiPartFormDataContent
    ): HttpResponse

    @POST("api/v1/posts/{id}")
    suspend fun updatePost(
        @Header("Authorization") token: String,
        @Path("id") id:Int,
        @Body map: MultiPartFormDataContent
    ): HttpResponse

    @POST("api/v1/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body map: MultiPartFormDataContent
    ): HttpResponse



}