package com.auth.ui.screens.signup

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.auth.ui.common_components.AuthScreenNavigator
import com.auth.ui.common_components.CombineKeys
import com.auth.ui.common_components.ConfirmButton
import com.auth.ui.common_components.CoupleDots
import com.auth.ui.common_components.TextFieldWithLabelAndValidation
import com.auth.ui.common_components.isEmailValid
import com.auth.ui.common_components.isPasswordValid
import com.auth.ui.common_components.isUsernameValid
import com.core.common.TestTags
import com.core.common.ui.theme.Red
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SignUpScreen(
    signUpModel: SignUpModel = hiltViewModel(),
    navigator: AuthScreenNavigator
) {

    val state by signUpModel.state.collectAsState()

    val context = LocalContext.current

    var passwordVisibility by remember {
        mutableStateOf(false)
    }
    var errorMessage: String? by remember {
        mutableStateOf(null)
    }


    val isFormValid by remember {
        derivedStateOf {
            isEmailValid(state.email) && isUsernameValid(state.username) && isFullNameValid(state.username) && isPasswordValid(
                state.password
            ) && arePasswordsMatching(
                password = state.password,
                confirmPassword = state.passwordConfirmation
            )
                    && state.country.isNotEmpty() && state.contactNumber.isNotEmpty() && state.address.isNotEmpty()
        }

    }
    DisposableEffect(key1 = state.success, key2 = state.error) {
        handleRegisterStatus(status = state, navigator, context) {
            if (it) errorMessage = "error while registration process: ${state.error}"
        }

        onDispose {
            signUpModel.updateStatus(state.copy(success = "", error = ""))
        }
    }

    LaunchedEffect(
        key1 = CombineKeys(
            state.email,
            state.username,
            state.fullName,
            state.password,
            state.passwordConfirmation
        )
    ) {
        errorMessage =
            if (state.username.isNotEmpty() && state.email.isNotEmpty() &&
                state.password.isNotEmpty() && state.passwordConfirmation.isNotEmpty()
            ) signUpInputErrorMessage(
                state
            ) else null
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .testTag(TestTags.SIGN_UP_SCREEN)
    ) {
        item {
            Row(
                modifier = Modifier.padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = "SIGN UP",
                        style = MaterialTheme.typography.displayMedium.copy(
                            color = MaterialTheme.colorScheme.tertiary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Spacer(modifier = Modifier.width(50.dp))
                TextButton(
                    onClick = { navigator.navigateToLoginScreen() },
                    modifier = Modifier.testTag(TestTags.NAVIGATION_BETWEEN_LOGIN_SIGNUP)
                ) {
                    Text(
                        text = "LOGIN",
                        style = MaterialTheme.typography.displaySmall.copy(
                            color = Color.Gray.copy(alpha = 0.4f),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            CoupleDots()

            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .testTag(TestTags.AUTH_ERROR_MESSAGE),
                contentAlignment = Alignment.Center
            ) {
                if (!errorMessage.isNullOrEmpty()) {
                    Text(
                        text = errorMessage ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.testTag("${TestTags.AUTH_ERROR_MESSAGE}error_message_text")
                    )
                }
            }
        }

        item {

            TextField(
                value = state.username, onValueChange = {
                    signUpModel.updateStatus(state.copy(username = it))
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(horizontal = 15.dp)
                    .padding(vertical = 10.dp)
                    .testTag(TestTags.SIGNUP_USERNAME_INPUT),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Red,
                    focusedLabelColor = Red,
                    textColor = Color.Black,
                    cursorColor = MaterialTheme.colorScheme.tertiary
                ),
                isError = !isUsernameValid(state.username) && state.username.isNotEmpty(),
                label = {
                    Text(text = "USERNAME")
                },
                placeholder = {
                    Text(text = "must be at least 3 char..", fontSize = 12.sp, maxLines = 1)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            TextField(
                value = state.fullName, onValueChange = {
                    signUpModel.updateStatus(state.copy(fullName = it))
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(horizontal = 15.dp)
                    .padding(vertical = 10.dp)
                    .testTag(TestTags.SIGNUP_FULLNAME_INPUT),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Red,
                    focusedLabelColor = Red,
                    textColor = Color.Black,
                    cursorColor = MaterialTheme.colorScheme.tertiary
                ),
                label = {
                    Text(text = "Full Name")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            TextField(
                value = state.country, onValueChange = {
                    signUpModel.updateStatus(state.copy(country = it))
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(horizontal = 15.dp)
                    .padding(vertical = 10.dp)
                    .testTag(TestTags.SIGNUP_FULLNAME_INPUT),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Red,
                    focusedLabelColor = Red,
                    textColor = Color.Black,
                    cursorColor = MaterialTheme.colorScheme.tertiary
                ),
                label = {
                    Text(text = "Country")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            TextField(
                value = state.contactNumber, onValueChange = {
                    signUpModel.updateStatus(state.copy(contactNumber = it))
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(horizontal = 15.dp)
                    .padding(vertical = 10.dp)
                    .testTag(TestTags.SIGNUP_FULLNAME_INPUT),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Red,
                    focusedLabelColor = Red,
                    textColor = Color.Black,
                    cursorColor = MaterialTheme.colorScheme.tertiary
                ),
                label = {
                    Text(text = "Contact number")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            TextField(
                value = state.address, onValueChange = {
                    signUpModel.updateStatus(state.copy(address = it))
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(horizontal = 15.dp)
                    .padding(vertical = 10.dp)
                    .testTag(TestTags.SIGNUP_FULLNAME_INPUT),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Red,
                    focusedLabelColor = Red,
                    textColor = Color.Black,
                    cursorColor = MaterialTheme.colorScheme.tertiary
                ),
                label = {
                    Text(text = "Address")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            TextFieldWithLabelAndValidation(
                value = state.email,
                onValueChange = {
                    signUpModel.updateStatus(state.copy(email = it))
                },
                isError = !isEmailValid(state.email) && state.email.isNotEmpty(),
                label = "Email",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.testTag(TestTags.SIGNUP_EMAIL_INPUT)
            )

            TextFieldWithLabelAndValidation(
                value = state.password,
                onValueChange = { signUpModel.updateStatus(state.copy(password = it)) },
                label = "Password",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = if (!passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisibility = !passwordVisibility },
                        modifier = Modifier.testTag(TestTags.PASSWORD_VISIBILITY_TOGGLE)
                    ) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = if (passwordVisibility) Red else Color.Gray
                        )
                    }
                },
                placeHolder = {
                    Text(text = "must be at least 8 char..", fontSize = 12.sp, maxLines = 1)
                },
                isError = !isPasswordValid(state.password) && state.password.isNotEmpty(),
                testTag = TestTags.AUTH_PASSWORD_INPUT
            )

            TextFieldWithLabelAndValidation(
                value = state.passwordConfirmation,
                onValueChange = { signUpModel.updateStatus(state.copy(passwordConfirmation = it)) },
                label = "Password Confirmation",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = PasswordVisualTransformation(),
                isError = !arePasswordsMatching(state.password, state.passwordConfirmation),
                modifier = Modifier.testTag(TestTags.SIGNUP_PASSWORD_CONFIRMATION_INPUT)
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ConfirmButton(
                    icon = Icons.Default.ArrowForward,
                    isEnabled = isFormValid,
                    testTag = TestTags.AUTH_CONFIRMATION_BUTTON
                ) {
                    signUpModel.registerUser()
                }
            }
        }
    }
}

private fun handleRegisterStatus(
    status: RegisterStates,
    navigator: AuthScreenNavigator,
    context: Context,
    errorMessage: (Boolean) -> Unit
) {
    if (!status.error.isNullOrEmpty()) {
        errorMessage(true)
        Toast.makeText(context, status.error, Toast.LENGTH_SHORT)
            .show()
        Log.d("PetErrors", "SignUpScreen:${status.error} ")
    }

    if (!status.success.isNullOrEmpty()) {
        errorMessage(false)
        Toast.makeText(context, status.success, Toast.LENGTH_SHORT).show()
        Log.d("Token", "SignUpScreen:${status.token} ")
        navigator.navigateToHomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    // SignUpScreen()
}