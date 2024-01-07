package com.core.network.auth_api.models

import com.core.network.DataResponse


data class LoginUserData(
    val password: String,
    val username_email: String,
    val token:String?=null
)


typealias LoginUserResponse = DataResponse<LoginUserData>
