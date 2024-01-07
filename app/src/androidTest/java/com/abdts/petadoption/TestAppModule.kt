package com.abdts.petadoption

import com.auth.domain.repository.AuthRepository
import com.auth.domain.use_cases.AuthUseCase
import com.abdts.petadoption.auth_screens.mock_classes.MockAuthCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {
    @Provides
    @Singleton
    fun provideAuthUseCase(authRepository: AuthRepository): AuthUseCase {
        return MockAuthCase(authRepository)
    }
}