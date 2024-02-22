package com.auth.ui

import com.auth.ui.common_components.isEmailValid
import com.auth.ui.common_components.isPasswordValid
import com.auth.ui.common_components.isUsernameValid
import com.auth.ui.screens.signup.RegisterStates
import com.auth.ui.screens.signup.arePasswordsMatching
import com.auth.ui.screens.signup.isTextFieldAreNotEmpty
import com.auth.ui.screens.signup.signUpInputErrorMessage
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SignUpValidationKtTest {

    @Test
    fun testUsernameValidation() {
        assertThat(isUsernameValid("az_bod")).isTrue()
        assertThat(isUsernameValid("!az_bod")).isFalse() // Contains special characters
        assertThat(isUsernameValid("az")).isFalse() // Invalid, less than 3 characters
    }

    @Test
    fun testFullFieldsValidation() {
        assertThat(isTextFieldAreNotEmpty("Adam Ozel","turkey","244552124","istanbul")).isTrue()
        assertThat(isTextFieldAreNotEmpty("","","","")).isFalse() // Invalid, empty string
    }

    @Test
    fun testEmailValidation() {
        assertThat(isEmailValid("adam@gmail.com")).isTrue()
        assertThat(isEmailValid("ad.gmail@com")).isFalse() // Invalid format
    }

    @Test
    fun testPasswordValidation() {
        assertThat(isPasswordValid("Password1")).isTrue()
        assertThat(isPasswordValid("short")).isFalse() // Invalid, less than 8 characters
        assertThat(isPasswordValid("nodigitg")).isFalse() // Invalid, no digit
        assertThat(isPasswordValid("12345678aA&")).isFalse() // Invalid, no character
    }

    @Test
    fun testPasswordsMatching() {
        assertThat(arePasswordsMatching("Password1", "Password1")).isTrue()
        assertThat(arePasswordsMatching("Password1", "Password2")).isFalse() // Not matching
    }

    @Test
    fun testSignUpInputErrorMessage() {
        val validStatus = RegisterStates(username = "az_bod", email = "adam@gmail.com", password = "Password1", passwordConfirmation = "Password1")

        val invalidUsernameStatus = RegisterStates(username = "!az", email = "adam@gmail.com", password="Password12", passwordConfirmation = "Password1")
        val invalidEmailStatus = RegisterStates(username = "az_bod", email = "adam.gmail@com", password =  "Password12&", passwordConfirmation = "Password1&")
        val invalidPasswordStatus = RegisterStates(username = "az_bod", email = "adam@gmail.com", password = "short", passwordConfirmation = "short")
        val passwordsNotMatchingStatus = RegisterStates(username = "az_bod", email = "adam@gmail.com", password = "Password1", passwordConfirmation =  "Password2")

        assertThat(signUpInputErrorMessage(validStatus)).isNull() // If null, there is no error message

        // Check specific error messages for each invalid case
        assertThat(signUpInputErrorMessage(invalidUsernameStatus))
            .isEqualTo("Username should be at least 3 chars without containing(!@#\$%^&*(),.?\":{}|<>)")

        assertThat(signUpInputErrorMessage(invalidEmailStatus))
            .isEqualTo("Email is not formatted correctly")

        assertThat(signUpInputErrorMessage(invalidPasswordStatus))
            .isEqualTo("Password should be at least 8 chars with one digit or character")

        assertThat(signUpInputErrorMessage(passwordsNotMatchingStatus))
            .isEqualTo("Passwords are not matching")
    }

}