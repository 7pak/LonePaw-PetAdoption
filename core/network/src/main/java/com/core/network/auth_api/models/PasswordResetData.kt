package com.core.network.auth_api.models

import com.google.gson.annotations.SerializedName

data class PasswordResetData(
    @SerializedName("password")
    val newPassword:String
)
