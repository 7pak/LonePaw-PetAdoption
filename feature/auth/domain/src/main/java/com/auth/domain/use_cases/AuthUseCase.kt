package com.auth.domain.use_cases

data class AuthUseCase(
    val registerUser: RegisterUser,
    val loginUser: LoginUser,
    val verifyEmail: VerifyEmail,
    val verifyOtp: VerifyOtp,
    val resendOtp: ResendOtp,
    val resetPassword: ResetPassword
    )
