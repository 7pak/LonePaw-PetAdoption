package com.core.network.auth_api.models

import com.google.gson.annotations.SerializedName

data class OtpVerificationData(
    @SerializedName("otp_code")
    val otp:String,
    @SerializedName("is_verified")
    val isVerified:Boolean?=null
)
