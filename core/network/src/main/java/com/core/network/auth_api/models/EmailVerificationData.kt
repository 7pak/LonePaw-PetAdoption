package com.core.network.auth_api.models

import com.core.network.DataResponse
import com.google.gson.annotations.SerializedName

data class EmailVerificationData(
    @SerializedName("email")
    val email:String,
    val token:String?=null
)

typealias EmailVerificationResponse = DataResponse<EmailVerificationData>
