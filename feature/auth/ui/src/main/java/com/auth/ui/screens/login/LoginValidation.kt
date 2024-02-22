package com.auth.ui.screens.login

import com.auth.ui.common_components.isEmailValid
import com.auth.ui.common_components.isPasswordValid
import com.auth.ui.common_components.isUsernameValid

enum class LoginInputType{
    USERNAME,
    EMAIL
}

fun isEmailOrUsername(input:String):LoginInputType?{
    if (isEmailValid(input)){
        return LoginInputType.EMAIL
    }
    if (isUsernameValid(input)){
        return LoginInputType.USERNAME
    }

    return null
}

fun loginInputErrorMessage(loginStates: LoginStates):String?{
    if (isEmailOrUsername(loginStates.emailOrUsername)==null){
        return "invalid username or email"
    }
    if (!isPasswordValid(loginStates.password,inLogin = true)){
        return "Invalid password"
    }
    return null
}