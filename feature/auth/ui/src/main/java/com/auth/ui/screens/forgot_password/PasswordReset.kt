package com.auth.ui.screens.forgot_password

import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.auth.ui.common_components.AuthScreenNavigator
import com.auth.ui.common_components.BackHandler
import com.auth.ui.common_components.ConfirmButton
import com.auth.ui.common_components.OutlinedTextFieldWithLabelAndValidation
import com.auth.ui.common_components.isPasswordValid
import com.core.common.R
import com.core.common.ui.theme.Beige
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun PasswordResetScreen(
    verificationModel: VerificationModel = hiltViewModel(),
    navigator: AuthScreenNavigator
) {

    val context = LocalContext.current
    var passwordVisibility by rememberSaveable {
        mutableStateOf(false)
    }

    val passwordHasReset by verificationModel.passwordHasReset.collectAsState(false)

    val state = verificationModel.state
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    if (dispatcher != null) {
        BackHandler(onBackPressed = {
            verificationModel.cancel()
            navigator.navigateToLoginScreen()
        }, dispatcher = dispatcher)
    }
    LaunchedEffect(passwordHasReset){
        if (passwordHasReset){
            Toast.makeText(context, "Password has reset successfully", Toast.LENGTH_SHORT).show()
            verificationModel.cancel()
            navigator.navigateToLoginScreen()
        }
    }

    DisposableEffect(key1 = state.error){
        if (!state.error.isNullOrEmpty()) {
            Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
        }

        onDispose {
            verificationModel.updateState(state.copy(error = null))
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

        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {


            Text(
                text = "Reset your password",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Beige
                ),
                modifier = Modifier.padding(top = 40.dp)

            )

            Text(
                text = "Password should be at least 8 chars with at least a digit and character and special char",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 7.dp)
            )

            Text(
                text = "type your new password",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Beige.copy(alpha = 0.5f)
                ),
                modifier = Modifier.padding(vertical = 10.dp)

            )

            OutlinedTextFieldWithLabelAndValidation(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 10.dp),
                value =state.newPassword,
                onValueChange = {
                    verificationModel.updateState(state.copy(newPassword = it))
                },
                placeHolder = {
                    Text(text = "Password")
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "password visibility",
                            tint = if (passwordVisibility) MaterialTheme.colorScheme.tertiary
                            else Color.Gray
                        )
                    }
                },
                isError = !isPasswordValid(state.newPassword),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )

            ConfirmButton(text = "Confirm", onClick = {
                verificationModel.resetPassword()
            }, isEnabled = isPasswordValid(state.newPassword), testTag = "")
        }
        if (state.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center),color = MaterialTheme.colorScheme.tertiary)
        }
    }
}
