package com.core.network.auth_api.models

import com.core.network.DataResponse
import com.google.gson.annotations.SerializedName


data class LoginUserData(
    val password: String,
    val username_email: String,
    val token:String?=null,
    @SerializedName("user_id")
    val userId:Int?=null
)


typealias LoginUserResponse = DataResponse<LoginUserData>
