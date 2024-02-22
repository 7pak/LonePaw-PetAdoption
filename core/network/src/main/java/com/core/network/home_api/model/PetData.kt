package com.core.network.home_api.model


import com.core.network.DataResponse
import com.google.gson.annotations.SerializedName

data class PetData(
    @SerializedName("address")
    val address: String,
    @SerializedName("category")
    val category: Int,
    @SerializedName("contact_number")
    val contactNumber: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("pet_age")
    val petAge: Int,
    @SerializedName("pet_breed")
    val petBreed: String,
    @SerializedName("pet_gender")
    val petGender: String,
    @SerializedName("owner_name")
    val ownerName:String,
    @SerializedName("owner_id")
    val ownerId:Int,
    @SerializedName("owner_profile")
    val ownerProfilePic:String,
    @SerializedName("pet_name")
    val petName: String,
    @SerializedName("pet_desc")
    val petDesc:String,
    @SerializedName("pet_photos")
    val petPhotos: List<String>,
    @SerializedName("pet_type")
    val petType: String,
    @SerializedName("pet_favorite")
    val petFavorite:Boolean
)

typealias GetPetDataResponse = DataResponse<List<PetData>>
