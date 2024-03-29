package com.core.database.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.core.database.dao.PetsDao
import com.core.database.model.PetInfo


@Database(entities = [PetInfo::class], version = 1, exportSchema = false)
@TypeConverters(PetTypeConverter::class)
abstract class PetDatabase: RoomDatabase() {

    abstract val petDao: PetsDao


    companion object{
        const val name = "pet_dp"
    }
}