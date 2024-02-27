package com.profile.ui.screens.profile_detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.common.R
import com.core.common.test_tags.AuthTestTags
import com.core.common.ui.theme.Beige
import com.core.common.ui.theme.Red
import com.profile.ui.screens.profile_detail.items.ConfirmProfileButtonItem
import com.profile.ui.screens.profile_detail.items.OutLineTextFieldItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun ResetPasswordScreen(
    profileDetailModel: ProfileDetailModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {

    var passwordVisibility by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current

    val context = LocalContext.current
    val state = profileDetailModel.state

    val passwordHasReset by profileDetailModel.passwordHasReset.collectAsState(false)

    LaunchedEffect(key1 = passwordHasReset) {
        if (passwordHasReset) {
            Toast.makeText(context, "Password has reset successfully", Toast.LENGTH_SHORT).show()
            navigator.navigateUp()
        }
    }

    DisposableEffect(key1 = state.serverMessage) {
        if (state.serverMessage.isNotEmpty()) {
            Toast.makeText(context, state.serverMessage, Toast.LENGTH_SHORT).show()
        }
        onDispose {
            profileDetailModel.updateProfileState(state.copy(serverMessage = ""))
        }
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
        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Beige,
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(
                    Alignment.TopCenter
                )
                .padding(vertical = 15.dp)
        )

        Column(
            Modifier
                .fillMaxSize()
                .padding(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Password should be at least 8 chars with at least a digit and character and special char",
                style = MaterialTheme.typography.bodyMedium.copy(color = Beige.copy(0.6f)),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 25.dp)
            )
            OutLineTextFieldItem(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 5.dp),
                value = state.oldPassword,
                onValueChange = { profileDetailModel.updateProfileState(state.copy(oldPassword = it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = PasswordVisualTransformation(),
                placeHolder = {
                    Text(text = "Old Password")
                }, focusManager = focusManager, label = "Old Password"
            )

            OutLineTextFieldItem(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 5.dp),
                value = state.newPassword,
                onValueChange = { profileDetailModel.updateProfileState(state.copy(newPassword = it)) },
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
                    Text(text = "New Password")
                }, focusManager = focusManager, label = "New Password"
            )

            ConfirmProfileButtonItem(
                icon = Icons.Default.Check,
                isEnabled = isPasswordValid(password = state.newPassword) && state.oldPassword.isNotEmpty(),
                modifier = Modifier.padding(vertical = 30.dp)
            ) {
                profileDetailModel.updatePassword()
            }
        }
        if (state.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center),color = MaterialTheme.colorScheme.tertiary)
        }
    }
}

private fun isPasswordValid(password: String, inLogin: Boolean = false): Boolean {
    val uppercaseRegex = "[A-Z]".toRegex()
    val lowercaseRegex = "[a-z]".toRegex()
    val digitRegex = "\\d".toRegex()
    val specialCharsRegex = "[!@#\$%^&*(),.?\":{}|<>]".toRegex()

    val containsUppercase = uppercaseRegex.containsMatchIn(password)
    val containsLowercase = lowercaseRegex.containsMatchIn(password)
    val containsDigit = digitRegex.containsMatchIn(password)
    val containSpecialChar = specialCharsRegex.containsMatchIn(password)

    if (inLogin) return password.isNotEmpty() && password.length >= 8

    return password.isNotEmpty() && containsUppercase && containsLowercase && password.length >= 8 && containsDigit && containSpecialChar
}