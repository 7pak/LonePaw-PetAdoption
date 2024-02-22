package com.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.core.database.data_source.PetTypeConverter
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
    val ownerId:Int,
    val ownerProfilePic:String?,
    val petDesc:String,
    @TypeConverters(PetTypeConverter::class)
    val petPhoto: List<String>?,
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
        ownerId = ownerId,
        ownerProfilePic = ownerProfilePic,
        petDesc = petDesc,
        petPhoto = petPhotos,
        petType = petType,
        petFavorite = petFavorite,
        petId = id
    )
}
