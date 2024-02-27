package com.auth.domain.di

import com.auth.domain.repository.AuthRepository
import com.auth.domain.use_cases.AuthUseCase
import com.auth.domain.use_cases.LoginUser
import com.auth.domain.use_cases.RegisterUser
import com.auth.domain.use_cases.ResendOtp
import com.auth.domain.use_cases.ResetPassword
import com.auth.domain.use_cases.VerifyEmail
import com.auth.domain.use_cases.VerifyOtp
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
    fun provideAuthUseCase(authRepository: AuthRepository): AuthUseCase {
        return AuthUseCase(
            registerUser = RegisterUser(authRepository),
            loginUser = LoginUser(authRepository),
            verifyEmail = VerifyEmail(authRepository),
            verifyOtp = VerifyOtp(authRepository),
            resendOtp = ResendOtp(authRepository),
            resetPassword = ResetPassword(authRepository)
        )
    }
}