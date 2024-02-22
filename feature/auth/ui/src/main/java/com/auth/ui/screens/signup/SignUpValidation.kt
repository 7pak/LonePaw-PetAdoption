package com.auth.ui.screens.signup

import com.auth.ui.common_components.isEmailValid
import com.auth.ui.common_components.isPasswordValid
import com.auth.ui.common_components.isUsernameValid

fun isTextFieldAreNotEmpty(fullName: String,country:String,contactNumber:String,address:String): Boolean {
    return fullName.isNotEmpty() &&country.isNotEmpty() && contactNumber.isNotEmpty() && address.isNotEmpty()
}

fun arePasswordsMatching(password: String, confirmPassword: String): Boolean {
    return password == confirmPassword
}


fun signUpInputErrorMessage(registerStates: RegisterStates):String?{
    if (!isUsernameValid(registerStates.username)) {
        return "Username should be at least 3 chars without containing(!@#\$%^&*(),.?\":{}|<>)"
    }
    if (!isEmailValid(registerStates.email)){
        return "Email is not formatted correctly"
    }
    if (!isPasswordValid(registerStates.password)){
        return "Password should be at least 8 chars with at least a digit and character and special char"
    }
    if (!arePasswordsMatching(password = registerStates.password, confirmPassword = registerStates.passwordConfirmation)){
        return "Passwords are not matching"
    }
    return null
}