package com.auth.ui.screens.signup

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.auth.ui.common_components.AuthScreenNavigator
import com.auth.ui.common_components.CombineKeys
import com.auth.ui.common_components.ConfirmButton
import com.auth.ui.common_components.CountryCodePickerDialog
import com.auth.ui.common_components.HyperLinkClickable
import com.auth.ui.common_components.OutlinedTextFieldWithLabelAndValidation
import com.auth.ui.common_components.isEmailValid
import com.auth.ui.common_components.isPasswordValid
import com.auth.ui.common_components.isUsernameValid
import com.core.common.R
import com.core.common.test_tags.AuthTestTags
import com.core.common.ui.theme.Beige
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
    val selectedCountry by signUpModel.selectedCountry.collectAsState()

    val context = LocalContext.current

    var passwordVisibility by remember {
        mutableStateOf(false)
    }
    var errorMessage: String? by remember {
        mutableStateOf(null)
    }


    val isFormValid by remember {
        derivedStateOf {
            isEmailValid(state.email) && isUsernameValid(state.username) && isTextFieldAreNotEmpty(
                fullName = state.fullName,
                country = selectedCountry.fullName,
                contactNumber = state.contactNumber,
                address = state.address
            ) && isPasswordValid(
                state.password
            ) && arePasswordsMatching(
                password = state.password,
                confirmPassword = state.passwordConfirmation
            )
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


    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.background_post),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .background(MaterialTheme.colorScheme.background)
        )
        LazyColumn(
            Modifier
                .fillMaxSize()
                .testTag(AuthTestTags.SIGN_UP_SCREEN),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Sign up",
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = Beige,
                        fontWeight = FontWeight.SemiBold
                    ), modifier = Modifier.padding(vertical = 15.dp)
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .testTag(AuthTestTags.AUTH_ERROR_MESSAGE),
                    contentAlignment = Alignment.Center
                ) {
                    if (!errorMessage.isNullOrEmpty()) {
                        Text(
                            text = errorMessage ?: "",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.testTag("${AuthTestTags.AUTH_ERROR_MESSAGE}error_message_text")
                        )
                    }
                }
            }

            item {

                OutlinedTextField(
                    value = state.username, onValueChange = {
                        signUpModel.updateStatus(state.copy(username = it))
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(horizontal = 15.dp)
                        .padding(vertical = 10.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .testTag(AuthTestTags.SIGNUP_USERNAME_INPUT),
                    shape = RoundedCornerShape(25.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        // textColor = Color.Black,
                        errorContainerColor = Color.White,
                        cursorColor = MaterialTheme.colorScheme.tertiary
                    ),
                    isError = !isUsernameValid(state.username) && state.username.isNotEmpty(),
                    placeholder = {
                        Text(text = "Username")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    value = state.fullName, onValueChange = {
                        signUpModel.updateStatus(state.copy(fullName = it))
                    },
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(horizontal = 15.dp)
                        .padding(vertical = 10.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .testTag(AuthTestTags.SIGNUP_FULLNAME_INPUT),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        // textColor = Color.Black,
                        cursorColor = MaterialTheme.colorScheme.tertiary
                    ),
                    placeholder = {
                        Text(text = "Full name")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    value = state.contactNumber, onValueChange = {
                        if (it.length <= 12 && selectedCountry.code.isNotEmpty()) {
                            signUpModel.updateStatus(state.copy(contactNumber = it))
                        }
                    },
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(horizontal = 15.dp)
                        .padding(vertical = 10.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .testTag(AuthTestTags.SIGNUP_CONTACT_NUMBER_INPUT),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        cursorColor = MaterialTheme.colorScheme.tertiary
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ), leadingIcon = {
                        Text(
                            text = if (selectedCountry.code.isEmpty()) "Country" else "${
                                getFlagEmojiFor(
                                    selectedCountry.nameCode
                                )
                            } +${selectedCountry.code}",
                            modifier = Modifier
                                .padding(start = 20.dp, end = 5.dp)
                                .clickable {
                                    signUpModel.updateStatus(state.copy(showDialog = true))
                                })
                    }
                )

                OutlinedTextField(
                    value = state.address, onValueChange = {
                        signUpModel.updateStatus(state.copy(address = it))
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(horizontal = 15.dp)
                        .padding(vertical = 10.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .testTag(AuthTestTags.SIGNUP_ADDRESS_INPUT),
                    shape = RoundedCornerShape(25.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        cursorColor = MaterialTheme.colorScheme.tertiary
                    ),
                    placeholder = {
                        Text(text = "Address")
                    },
                    maxLines = 2,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextFieldWithLabelAndValidation(
                    value = state.email,
                    onValueChange = {
                        signUpModel.updateStatus(state.copy(email = it))
                    },
                    isError = !isEmailValid(state.email) && state.email.isNotEmpty(),
                    placeHolder = {
                        Text(text = "Email")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .testTag(AuthTestTags.SIGNUP_EMAIL_INPUT)
                        .fillMaxWidth(0.8f)
                        .padding(horizontal = 15.dp)
                        .padding(vertical = 10.dp),
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.AlternateEmail, contentDescription = null)
                    }
                )

                OutlinedTextFieldWithLabelAndValidation(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(horizontal = 15.dp)
                        .padding(vertical = 10.dp),
                    value = state.password,
                    onValueChange = { signUpModel.updateStatus(state.copy(password = it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = if (!passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisibility = !passwordVisibility },
                            modifier = Modifier.testTag(AuthTestTags.PASSWORD_VISIBILITY_TOGGLE)
                        ) {
                            Icon(
                                imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = if (passwordVisibility) Red else Color.Gray
                            )
                        }
                    },
                    placeHolder = {
                        Text(text = "Password")
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Password, contentDescription = null)
                    },
                    isError = !isPasswordValid(state.password) && state.password.isNotEmpty(),
                    testTag = AuthTestTags.AUTH_PASSWORD_INPUT
                )

                OutlinedTextFieldWithLabelAndValidation(
                    value = state.passwordConfirmation,
                    onValueChange = { signUpModel.updateStatus(state.copy(passwordConfirmation = it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    placeHolder = {
                        Text(text = "Confirm Password")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = !arePasswordsMatching(state.password, state.passwordConfirmation),
                    modifier = Modifier
                        .testTag(AuthTestTags.SIGNUP_PASSWORD_CONFIRMATION_INPUT)
                        .fillMaxWidth(0.8f)
                        .padding(horizontal = 15.dp)
                        .padding(vertical = 10.dp),
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    ConfirmButton(
                        modifier = Modifier.fillMaxWidth(0.7f),
                        text = "Sign up",
                        isEnabled = isFormValid,
                        testTag = AuthTestTags.AUTH_CONFIRMATION_BUTTON
                    ) {
                        signUpModel.registerUser()
                    }
                }


                HyperLinkClickable(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = "Have an account? ",
                    linkText = "Login",
                    linkColor = MaterialTheme.colorScheme.tertiary
                ) {
                    navigator.navigateToLoginScreen()
                }
            }
        }
        if (state.showDialog) {
            CountryCodePickerDialog(
                signUpModel.countriesList,
                onSelection = { country ->
                    signUpModel.updateCountry(country)
                }
            ) {
                signUpModel.updateStatus(state.copy(showDialog = false))
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.align(
                    Alignment.Center
                )
            )
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
        navigator.navigateToHomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    // SignUpScreen()
}