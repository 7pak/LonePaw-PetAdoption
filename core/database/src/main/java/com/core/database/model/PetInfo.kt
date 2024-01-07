package com.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.core.network.home_api.model.PetData

@Entity(tableName = "Pet_Info")
data class PetInfo(
    val address: String,
    val category: Int,
    val contactNumber: String,
    val country: String,
    val createdAt: String,
    val petAge: Int,
    val petBreed: String,
    val petGender: String,
    val petName: String,
    val ownerName:String,
    val petDesc:String,
    val petPhoto: String,
    val petType: String,
    val petFavorite:Boolean,
    @PrimaryKey
    val petId: Int,
)

fun PetData.toPetInfo(): PetInfo {
    return PetInfo(
        address = address,
        category = category,
        contactNumber = contactNumber,
        createdAt = createdAt,
        country = country,
        petAge = petAge,
        petBreed = petBreed,
        petGender = petGender,
        petName = petName,
        ownerName = ownerName,
        petDesc = petDesc,
        petPhoto = petPhoto,
        petType = petType,
        petFavorite = petFavorite,
        petId = id
    )
}
