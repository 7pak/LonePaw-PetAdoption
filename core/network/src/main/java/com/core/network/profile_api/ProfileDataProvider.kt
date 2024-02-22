package com.core.network.profile_api

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import com.core.common.Constants
import com.core.network.DataResponse
import com.core.network.home_api.HomeApi
import com.core.network.home_api.model.GetPetDataResponse
import com.core.network.home_api.model.PetData
import com.core.network.profile_api.model.AddPostData
import com.core.network.profile_api.model.GetProfileDataResponse
import com.core.network.profile_api.model.UpdateProfileData
import io.ktor.client.request.forms.FormBuilder
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ProfileDataProvider @Inject constructor(
    private val profileApi: ProfileApi,
    private val uploadProfileApi: UploadProfileApi,
    private val homeApi: HomeApi,
    private val tokenFlow: Flow<String?>,
    private val context: Context
) {
    private suspend fun getToken(): String? {
        return tokenFlow.firstOrNull()
    }

    suspend fun addPost(
        addPostData: AddPostData
    ): HttpResponse {


        val externalCacheDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)


        val files = mutableListOf<File>()

        try {
            withContext(Dispatchers.IO) {
                addPostData.petPhotos?.forEach { uri ->
                    val newFileName = Constants.POST_IMAGE_NAME+files.size
                    val file = File(externalCacheDir, newFileName)
                    val inputStream =
                        context.contentResolver.openInputStream(uri.toUri())
                    val outputStream = FileOutputStream(file)
                    inputStream?.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }
                    files.add(file)
                }
            }
        } catch (e: Exception) {
            Log.d("ErrorTag", "input output streams error: ${e.localizedMessage} ")
        }


        val multipart = MultiPartFormDataContent(formData {
            files.forEach { file ->
                append("pet_photo[]", file.readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, "multipart/form-data")
                    append(HttpHeaders.Accept, "application/json")
                    append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                }
                )
            }

            append("pet_name", addPostData.petName)
            append("pet_type", addPostData.petType)
            append("category_id", addPostData.categoryId)
            append("pet_gender", addPostData.petGender)
            append("pet_breed", addPostData.petBreed)
            append("pet_age", addPostData.petAge)
            append("pet_desc", addPostData.petDesc)
        }
        )

        return uploadProfileApi.addPost(
            "Bearer ${getToken()}",
            multipart
        )
    }

    suspend fun updatePost(
        id: Int,
        addPostData: AddPostData
    ): HttpResponse {


        val externalCacheDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val files = mutableListOf<File>()

        try {
            withContext(Dispatchers.IO) {
                addPostData.petPhotos?.forEach { uri ->
                    val newFileName = Constants.POST_IMAGE_NAME+files.size
                    val file = File(externalCacheDir, newFileName)
                    val inputStream =
                        context.contentResolver.openInputStream(uri.toUri())
                    val outputStream = FileOutputStream(file)
                    inputStream?.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }
                    files.add(file)
                }
            }
        } catch (e: Exception) {
            Log.d("ErrorTag", "input output streams error: ${e.localizedMessage} ")
        }


        val multipart = MultiPartFormDataContent(formData {
            files.forEach { file ->
                append("pet_photo[]", file.readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, "multipart/form-data")
                    append(HttpHeaders.Accept, "application/json")
                    append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                }
                )
            }

            append("pet_name", addPostData.petName)
            append("pet_type", addPostData.petType)
            append("category_id", addPostData.categoryId)
            append("pet_gender", addPostData.petGender)
            append("pet_breed", addPostData.petBreed)
            append("pet_age", addPostData.petAge)
            append("pet_desc", addPostData.petDesc)
            append("_method", "PUT")
        }
        )

        return uploadProfileApi.updatePost(
            "Bearer ${getToken()}",
            id = id,
            multipart
        )
    }

    suspend fun deletePost(id: Int): DataResponse<*> {
        return profileApi.deletePost("Bearer ${getToken()}", id)
    }

    suspend fun getMyPets(): GetPetDataResponse {
        return profileApi.getMyPets("Bearer ${getToken()}")
    }

    suspend fun getPet(id: Int): DataResponse<PetData> {
        return homeApi.getPet(id = id, token = "Bearer ${getToken()}")
    }

    suspend fun getFavorite(): GetPetDataResponse {
        return profileApi.getFavorites("Bearer ${getToken()}")
    }


    suspend fun removeFavorite(id: Int): DataResponse<*> {
        return profileApi.removeFavorite("Bearer ${getToken()}", id)
    }

    suspend fun getProfile(): GetProfileDataResponse {
        return profileApi.getProfile("Bearer ${getToken()}")
    }

    suspend fun updateProfile(
        updateProfileData: UpdateProfileData
    ): HttpResponse {


        val externalCacheDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val newFileName = Constants.POST_IMAGE_NAME
        val file = File(externalCacheDir, newFileName)

        if (updateProfileData.profilePic != null) {
            try {
                withContext(Dispatchers.IO) {
                    val inputStream =
                        context.contentResolver.openInputStream(updateProfileData.profilePic)
                    val outputStream = FileOutputStream(file)
                    inputStream?.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("ErrorTag", "input output streams error: ${e.localizedMessage} ")
            }
        }


        val multipart = MultiPartFormDataContent(formData {
            if (file.exists()) {
                append("profile", file.readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, "multipart/form-data")
                    append(HttpHeaders.Accept, "application/json")
                    append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                }
                )
            }
            append("name_", updateProfileData.name)
            append("username", updateProfileData.username)
            append("email", updateProfileData.email)
            append("contact_number", updateProfileData.contactNumber)
            append("country", updateProfileData.country)
            append("address", updateProfileData.address)
            append("_method", "PUT")
        }
        )

        return uploadProfileApi.updateProfile(
            "Bearer ${getToken()}",
            multipart
        )
    }
}