package com.home.data.di

import com.core.network.home_api.HomeDataProvider
import com.home.data.repository.HomeRepositoryImpl
import com.home.domain.repository.HomeRepositroy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HomeDataModule {
    @Provides
    fun providesHomeRepository(homeDataProvider: HomeDataProvider): HomeRepositroy {
        return HomeRepositoryImpl(homeDataProvider)
    }
}