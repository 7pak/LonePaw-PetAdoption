package com.auth.ui.screens.login

data class LoginStates(
    val emailOrUsername:String = "",
    val password:String = "",
    val isLoading:Boolean = false,
    val success:String?=null,
    val token:String?=null,
    val error:String?=null,
)
