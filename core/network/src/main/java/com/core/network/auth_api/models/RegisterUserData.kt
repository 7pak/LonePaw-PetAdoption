package com.core.network.auth_api.models

import com.core.network.DataResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Response


data class RegisterUserData(
    @SerializedName("name_")
    val name:String,
    val username: String,
    val email: String,
    val password: String,
    @SerializedName("contact_number")
    val contactNumber:String,
    val country:String,
    val address:String,
    val token:String? =null,
    @SerializedName("user_id")
    val userId:Int?=null
)
typealias RegisterUserResponse = Response<DataResponse<RegisterUserData>>