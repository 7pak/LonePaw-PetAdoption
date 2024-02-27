package com.abdts.petadoption

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {
//    @Provides
//    @Singleton
//    fun provideAuthUseCase(authRepository: AuthRepository): AuthUseCase {
//        return MockAuthCase(authRepository)
//    }


}