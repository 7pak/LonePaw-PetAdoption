package com.home.domain

import com.core.database.dao.PetsDao
import com.core.network.home_api.HomeDataProvider
import com.home.domain.use_cases.AddFavorite
import com.home.domain.use_cases.GetPet
import com.home.domain.use_cases.GetPosts
import com.home.domain.use_cases.GetPostsCategory
import com.home.domain.use_cases.GetProfileHome
import com.home.domain.use_cases.GetSearchResult
import com.home.domain.use_cases.HomeUseCase
import com.home.domain.use_cases.RemoveFavoriteHome
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object HomeDomainLayer {

    @Provides
    fun providesHomeRepository(homeDataProvider: HomeDataProvider): HomeRepositroy {
        return HomeRepositoryImpl(homeDataProvider)
    }

    @Provides
    fun provideHomeUseCase(homeRepository: HomeRepositroy, dao: PetsDao): HomeUseCase {
        return HomeUseCase(
            getPosts = GetPosts(homeRepository, dao),
            getPet = GetPet(homeRepository, dao),
            addFavorite = AddFavorite(homeRepository),
            removeFavoriteHome = RemoveFavoriteHome(homeRepository),
            getProfileHome = GetProfileHome(homeRepository),
            getSearchResult = GetSearchResult(homeRepository),
            getPostsCategory = GetPostsCategory(homeRepository)
        )
    }
}