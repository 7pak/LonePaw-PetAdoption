package com.feature.profile.data.di

import com.core.network.profile_api.ProfileDataProvider
import com.feature.profile.data.repository.ProfileRepositoryImpl
import com.feature.profile.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileDataModule {

    @Singleton
    @Provides
    fun provideProfileRepository(provider: ProfileDataProvider): ProfileRepository {
        return ProfileRepositoryImpl(profileDataProvider = provider)
    }
}