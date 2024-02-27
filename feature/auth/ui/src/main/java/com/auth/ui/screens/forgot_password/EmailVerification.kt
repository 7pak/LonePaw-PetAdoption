package com.auth.ui.screens.forgot_password

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.auth.ui.common_components.AuthScreenNavigator
import com.auth.ui.common_components.ConfirmButton
import com.auth.ui.common_components.OutlinedTextFieldWithLabelAndValidation
import com.auth.ui.common_components.isEmailValid
import com.core.common.R
import com.core.common.ui.theme.Beige
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun EmailVerificationScreen(
    verificationModel: VerificationModel = hiltViewModel(),
    navigator: AuthScreenNavigator
) {

    val context = LocalContext.current

    val state = verificationModel.state


    val otpHasSent by verificationModel.otpHasSent.collectAsState(false)

    LaunchedEffect(key1 = otpHasSent,state.error){
        if (otpHasSent){
            Toast.makeText(context, "Otp has sent", Toast.LENGTH_SHORT).show()
            navigator.navigateToOtpVerificationScreen()
        }
        if (!state.error.isNullOrEmpty()){
            Toast.makeText(context, "Error occurred: ${state.error}", Toast.LENGTH_SHORT).show()
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
                text = "Help us know who you are ",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Beige
                ),
                modifier = Modifier.padding(top = 40.dp)

            )

            Text(
                text = "type your email",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Beige.copy(alpha = 0.5f)
                ),
                modifier = Modifier.padding(vertical = 10.dp)

            )

            OutlinedTextFieldWithLabelAndValidation(
                modifier = Modifier.padding(top = 30.dp),
                value = state.email,
                onValueChange = {
                    verificationModel.updateState(state.copy(email = it))
                },
                placeHolder = {
                    Text(text = "Email")
                },
                isError = !isEmailValid(state.email),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                )
            )

            ConfirmButton(text = "Next", onClick = {
               verificationModel.verifyEmail()
            }, isEnabled = isEmailValid(state.email) && state.email.isNotEmpty(), testTag = ""
            )

        }

        if (state.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center),color = MaterialTheme.colorScheme.tertiary)
        }
    }
}