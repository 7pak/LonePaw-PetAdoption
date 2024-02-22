package com.core.network.profile_api.model


import com.google.gson.annotations.SerializedName

data class AddPostData(
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("pet_age")
    val petAge: Int,
    @SerializedName("pet_breed")
    val petBreed: String,
    @SerializedName("pet_desc")
    val petDesc: String,
    @SerializedName("pet_gender")
    val petGender: String,
    @SerializedName("pet_name")
    val petName: String,
    @SerializedName("pet_photos")
    val petPhotos: List<String>?,
    @SerializedName("pet_type")
    val petType: String
)

