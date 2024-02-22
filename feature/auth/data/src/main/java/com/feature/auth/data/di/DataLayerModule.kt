package com.feature.auth.data.di

import com.auth.domain.repository.AuthRepository
import com.core.network.auth_api.AuthDataProvider
import com.core.network.chat_api.ChatDataProvider
import com.feature.auth.data.repository.AuthRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataLayerModule {

    @Provides
    @Singleton
    fun provideAuthRepository(authDataProvider: AuthDataProvider):AuthRepository{
        return AuthRepositoryImpl(authDataProvider = authDataProvider)
    }
}