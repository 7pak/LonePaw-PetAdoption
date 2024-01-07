package com.auth.ui.screens.login

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.auth.ui.common_components.AuthScreenNavigator
import com.auth.ui.common_components.ConfirmButton
import com.auth.ui.common_components.CoupleDots
import com.auth.ui.common_components.TextFieldWithLabelAndValidation
import com.auth.ui.common_components.isPasswordValid
import com.core.common.TestTags
import com.core.common.ui.theme.Red
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@OptIn(ExperimentalMaterial3Api::class)
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
        handleLoginStatus(status = state,navigator, context){
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
            isEmailOrUsername(state.emailOrUsername) != null && isPasswordValid(state.password)
        }
    }

    LaunchedEffect(key1 = state.emailOrUsername,key2 = state.password) {
        errorMessage =
            if (state.emailOrUsername.isNotEmpty() && state.password.isNotEmpty()) loginInputErrorMessage(
                state
            ) else null
    }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .testTag(TestTags.LOGIN_SCREEN)
    ) {
        item {
            Row(
                modifier = Modifier.padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = "LOGIN",
                        style = MaterialTheme.typography.displayMedium.copy(
                            color = MaterialTheme.colorScheme.tertiary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Spacer(modifier = Modifier.width(50.dp))
                TextButton(onClick = { navigator.navigateToSignUpScreen() },modifier = Modifier.testTag(TestTags.NAVIGATION_BETWEEN_LOGIN_SIGNUP)) {
                    Text(
                        text = "SIGN UP",
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
                value = state.emailOrUsername, onValueChange = {
                    loginModel.updateStatus(state.copy(emailOrUsername = it))
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(horizontal = 15.dp)
                    .padding(vertical = 10.dp)
                    .testTag(TestTags.LOGIN_EMAIL_OR_USERNAME_INPUT),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                    textColor = Color.Black,
                    cursorColor = MaterialTheme.colorScheme.tertiary
                ),
                label = {
                    Text(text = "USERNAME OR EMAIL")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )


            TextFieldWithLabelAndValidation(
                value = state.password,
                onValueChange = { loginModel.updateStatus(state.copy(password = it)) },
                label = "Password",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = if (!passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility },modifier = Modifier.testTag(TestTags.PASSWORD_VISIBILITY_TOGGLE)) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = if (passwordVisibility) Red else Color.Gray
                        )
                    }
                },
                testTag = TestTags.AUTH_PASSWORD_INPUT
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                ConfirmButton(icon = Icons.Default.ArrowForward, isEnabled = isFormValid, testTag = TestTags.AUTH_CONFIRMATION_BUTTON) {
                    loginModel.loginUser()
                }
            }
        }
    }
}

private fun handleLoginStatus(status:LoginStates,navigator: AuthScreenNavigator,context:Context,errorMessage:(Boolean)->Unit){
    if (!status.error.isNullOrEmpty()) {
        errorMessage(true)
        Toast.makeText(context, status.error, Toast.LENGTH_SHORT)
            .show()
        Log.d("PetErrors", "LoginScreen:${status.error} ")
    }

    if (!status.success.isNullOrEmpty()) {
        errorMessage(false)
        Toast.makeText(context, status.success, Toast.LENGTH_SHORT).show()
        Log.d("Token", "LoginScreen:${status.token} ")
        navigator.navigateToHomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // LoginScreen()
}