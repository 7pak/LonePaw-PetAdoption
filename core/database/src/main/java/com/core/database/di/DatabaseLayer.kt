package com.core.database.di

import android.content.Context
import androidx.room.Room
import com.core.database.dao.PetsDao
import com.core.database.data_source.PetDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseLayer {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): PetDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = PetDatabase::class.java,
            name = "pet_dp"
        ).build()
    }

    @Singleton
    @Provides
    fun providePetDao(database: PetDatabase): PetsDao {
        return database.petDao
    }


}