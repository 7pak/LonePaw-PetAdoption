package com.auth.ui.screens.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.auth.ui.common_components.AuthScreenNavigator
import com.auth.ui.common_components.ConfirmButton
import com.auth.ui.common_components.HyperLinkClickable
import com.auth.ui.common_components.OutlinedTextFieldWithLabelAndValidation
import com.auth.ui.common_components.isPasswordValid
import com.core.common.test_tags.AuthTestTags
import com.core.common.R
import com.core.common.ui.theme.Beige
import com.core.common.ui.theme.Red
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@Destination
@Composable
fun LoginScreen(
    navigator: AuthScreenNavigator,
    loginModel: LoginModel = hiltViewModel()
) {

    val state by loginModel.state.collectAsState()

    val context = LocalContext.current

    var errorMessage: String? by remember {
        mutableStateOf(null)
    }


    DisposableEffect(key1 = state.success, key2 = state.error) {
        handleLoginStatus(status = state, navigator, context) {
            if (it) errorMessage = "error while logging in: incorrect username or password"
        }

        onDispose {
            loginModel.updateStatus(state.copy(success = "", error = ""))
        }
    }

    var passwordVisibility by remember {
        mutableStateOf(false)
    }


    val isFormValid by remember {
        derivedStateOf {
            isEmailOrUsername(state.emailOrUsername) != null && isPasswordValid(
                state.password,
                inLogin = true
            )
        }
    }

    LaunchedEffect(key1 = state.emailOrUsername, key2 = state.password) {
        errorMessage =
            if (state.emailOrUsername.isNotEmpty() && state.password.isNotEmpty()) loginInputErrorMessage(
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
                .testTag(AuthTestTags.LOGIN_SCREEN),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = Beige,
                        fontWeight = FontWeight.SemiBold
                    ), modifier = Modifier.padding(vertical = 30.dp)
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

                OutlinedTextFieldWithLabelAndValidation(
                    value = state.emailOrUsername, onValueChange = {
                        loginModel.updateStatus(state.copy(emailOrUsername = it))
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(horizontal = 15.dp)
                        .padding(vertical = 10.dp)
                        .testTag(AuthTestTags.LOGIN_EMAIL_OR_USERNAME_INPUT),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    placeHolder = {
                        Text(text = "Username/Email", color = Color.Gray.copy(0.4f))
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = if (isEmailOrUsername(state.emailOrUsername) == null || state.emailOrUsername.isEmpty())
                                Icons.Default.CheckCircleOutline else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )

                    }
                )




                OutlinedTextFieldWithLabelAndValidation(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(horizontal = 15.dp),
                    value = state.password,
                    onValueChange = { loginModel.updateStatus(state.copy(password = it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
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
                    testTag = AuthTestTags.AUTH_PASSWORD_INPUT,
                    placeHolder = {
                        Text(text = "password", color = Color.Gray.copy(0.4f))
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Password, contentDescription = null)
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(30.dp))

                ConfirmButton(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    text = "Login",
                    isEnabled = isFormValid,
                    testTag = AuthTestTags.AUTH_CONFIRMATION_BUTTON
                ) {
                    loginModel.loginUser()
                }

                TextButton(onClick = {
                    navigator.navigateToEmailVerificationScreen()
                }, modifier = Modifier.padding(vertical = 15.dp)) {
                    Text(
                        text = "Forgot your password?",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    )
                }


            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(70.dp)
                            .padding(vertical = 8.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.tertiary,
                                shape = RoundedCornerShape(25.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.login_google),
                            contentDescription = "login with google",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(30.dp)
                                .padding(horizontal = 5.dp)
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = Color.Red)) {
                                    append("Login ")
                                }
                                withStyle(style = SpanStyle(color = Color.Green)) {
                                    append("with ")
                                }
                                withStyle(style = SpanStyle(color = Color.Blue)) {
                                    append("Google")
                                }
                            },
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    HyperLinkClickable(
                        modifier = Modifier.padding(vertical = 10.dp),
                        text = "Don't have an Account? ",
                        linkText = "Sign up",
                        linkColor = MaterialTheme.colorScheme.tertiary
                    ) {
                        navigator.navigateToSignUpScreen()
                    }
                }
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

private fun handleLoginStatus(
    status: LoginStates,
    navigator: AuthScreenNavigator,
    context: Context,
    errorMessage: (Boolean) -> Unit
) {
    if (!status.error.isNullOrEmpty()) {
        errorMessage(true)
        Toast.makeText(context, status.error, Toast.LENGTH_SHORT)
            .show()
        Log.d("PetErrors", "LoginScreen:${status.error} ")
    }

    if (!status.success.isNullOrEmpty()) {
        errorMessage(false)
        Toast.makeText(context, status.success, Toast.LENGTH_SHORT).show()
        navigator.navigateToHomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // LoginScreen()
}