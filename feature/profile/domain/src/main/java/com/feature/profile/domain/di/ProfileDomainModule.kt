package com.feature.profile.domain.di

import com.feature.profile.domain.repository.ProfileRepository
import com.feature.profile.domain.use_cases.AddPost
import com.feature.profile.domain.use_cases.DeletePost
import com.feature.profile.domain.use_cases.GetFavoritePets
import com.feature.profile.domain.use_cases.GetMyPets
import com.feature.profile.domain.use_cases.GetProfile
import com.feature.profile.domain.use_cases.ProfileUseCase
import com.feature.profile.domain.use_cases.RemoveFavorite
import com.feature.profile.domain.use_cases.UpdatePost
import com.feature.profile.domain.use_cases.UpdateProfile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileDomainModule {

    @Provides
    @Singleton
    fun provideProfileUseCase(profileRepository: ProfileRepository): ProfileUseCase {
        return ProfileUseCase(
            getMyPets = GetMyPets(profileRepository),
            getFavoritePets = GetFavoritePets(profileRepository),
            removeFavorite = RemoveFavorite(profileRepository),
            addPost = AddPost(profileRepository),
            updatePost = UpdatePost(profileRepository),
            deletePost = DeletePost(profileRepository),
            getProfile = GetProfile(profileRepository),
            updateProfile = UpdateProfile(profileRepository)
        )
    }
}