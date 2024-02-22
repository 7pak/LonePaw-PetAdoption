package com.core.database.data_source

import androidx.room.TypeConverter

class PetTypeConverter {
    @TypeConverter
    fun fromPetPhotoList(value: List<String>?): String? = value?.joinToString(",")

    @TypeConverter
    fun toPetPhotoList(value: String?): List<String>? = value?.split(",")

}