package com.core.network.profile_api.model

import com.google.gson.annotations.SerializedName

data class UpdatePasswordData(
    @SerializedName("old_password")
    val oldPassword:String,
    @SerializedName("new_password")
    val newPassword:String
)
