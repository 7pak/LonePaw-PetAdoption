package com.home.ui.screens.pet_details

data class PetDetailState(
    val address: String? ="",
    val contactNumber: String="",
    val country: String="",
    val createdAt: String="",
    val petAge: Int?=null,
    val petBreed: String="",
    val petGender: String="",
    val petName: String="",
    val ownerName:String="",
    val ownerId:Int?=null,
    val ownerPhoto:String? = "",
    val petDesc:String="",
    val petPhotos: List<String> = emptyList(),
    val petType:String="",
    val petFavorite:Boolean=false,
    val id: Int?=null,
    val isLoading:Boolean = false
)
