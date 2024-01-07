package com.auth.ui

import com.auth.ui.screens.login.LoginInputType
import com.auth.ui.screens.login.LoginStates
import com.auth.ui.screens.login.isEmailOrUsername
import com.auth.ui.screens.login.loginInputErrorMessage
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LoginValidationKtTest {

    @Test
    fun testIsEmailOrUsername() {
        val email = "adam@gmail.com"
        val username = "az_bod"
        val invalidInput = "az.com@gmail"

        assertThat(isEmailOrUsername(email)).isEqualTo(LoginInputType.EMAIL)
        assertThat(isEmailOrUsername(username)).isEqualTo(LoginInputType.USERNAME)
        assertThat(isEmailOrUsername(invalidInput)).isNull()
    }

    @Test
    fun testLoginInputErrorMessage() {
        val validInput = LoginStates(emailOrUsername = "az_bod", password = "12345678a")
        val invalidUsernameOrEmail = LoginStates(emailOrUsername = "az.com@gmail", password = "12345678a")
        val invalidPassword = LoginStates(emailOrUsername = "adam@gmail.com", password = "short")

        assertThat(loginInputErrorMessage(validInput)).isNull()
        assertThat(loginInputErrorMessage(invalidUsernameOrEmail)).isEqualTo("invalid username or email")
        assertThat(loginInputErrorMessage(invalidPassword)).isEqualTo("Invalid password")
    }
}