package com.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.core.database.model.PetInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface PetsDao {

    @Upsert
    suspend fun upsertPet(petInfo: List<PetInfo>)

    @Query("SELECT * FROM PET_INFO ORDER BY strftime('%s', createdAt) DESC")
    fun getAllPets(): Flow<List<PetInfo>>

    @Query("SELECT * FROM PET_INFO WHERE petId=:petId")
    suspend fun getPetById(petId:Int): PetInfo

    @Query("DELETE FROM PET_INFO")
    fun deleteAll()
}