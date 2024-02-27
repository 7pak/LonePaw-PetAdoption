package com.auth.ui.screens.forgot_password

data class VerificationStates(
    val email:String = "",
    val newPassword:String = "",
    val otp:String = "",
    val isLoading:Boolean = false,
    val error:String?=null
)
