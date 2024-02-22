package com.core.network.profile_api.model


import com.core.network.DataResponse
import com.core.network.home_api.model.PetData
import com.google.gson.annotations.SerializedName

data class ProfileData(
    @SerializedName("address")
    val address: String,
    @SerializedName("contact_number")
    val contactNumber: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("profile")
    val profilePic: String?
)

typealias GetProfileDataResponse = DataResponse<ProfileData>
