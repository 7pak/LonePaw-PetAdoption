package com.auth.domain.di

import com.auth.domain.repository.AuthRepository
import com.auth.domain.use_cases.AuthUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DomainLayerModule {

    @Provides
    @Singleton
    fun provideAuthUseCase(authRepository: AuthRepository):AuthUseCase{
        return AuthUseCase(authRepository)
    }
}