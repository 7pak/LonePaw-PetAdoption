package com.core.network.profile_api.model


import android.net.Uri
import com.google.gson.annotations.SerializedName

data class UpdateProfileData(
    @SerializedName("address")
    val address: String,
    @SerializedName("contact_number")
    val contactNumber: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name_")
    val name: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("profile")
    val profilePic: Uri?
)