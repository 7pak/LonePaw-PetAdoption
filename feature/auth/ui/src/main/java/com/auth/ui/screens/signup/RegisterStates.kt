package com.auth.ui.screens.signup

data class RegisterStates(
    val fullName:String = "",
    val username:String = "",
    val email:String = "",
    val password:String = "",
    val passwordConfirmation:String = "",
    val contactNumber:String = "",
    val address:String = "",
    val isLoading:Boolean = false,
    val showDialog:Boolean = false,
    val success:String?=null,
    val error:String?=null
)
