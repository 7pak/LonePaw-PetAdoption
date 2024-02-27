package com.auth.ui.screens.forgot_password

import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.auth.ui.common_components.AuthScreenNavigator
import com.auth.ui.common_components.BackHandler
import com.auth.ui.common_components.ConfirmButton
import com.auth.ui.common_components.HyperLinkClickable
import com.auth.ui.common_components.OutlinedTextFieldWithLabelAndValidation
import com.core.common.R
import com.core.common.ui.theme.Beige
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun OtpVerificationScreen(
    verificationModel: VerificationModel = hiltViewModel(),

    navigator: AuthScreenNavigator
) {
    val context = LocalContext.current
    val state = verificationModel.state

    val otpIsCorrect by verificationModel.otpIsCorrect.collectAsState(false)
    val remainingTime by verificationModel.remainingTime.collectAsStateWithLifecycle()

    val timer: CountDownTimer = object : CountDownTimer(remainingTime, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            verificationModel.updateRemainingTime(millisUntilFinished)
        }

        override fun onFinish() {
            Toast.makeText(
                context,
                "time up!: opt code is invalid please resend for new one",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    DisposableEffect(Unit) {
        timer.start()

        // Cleanup when the composable is disposed
        onDispose {
            timer.cancel()
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

    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    if (dispatcher != null) {
        BackHandler(
            onBackPressed = {
                verificationModel.cancel()
                navigator.navigateToLoginScreen()
            },
            dispatcher = dispatcher,
        )
    }


    LaunchedEffect(key1 = otpIsCorrect, state.error) {
        if (otpIsCorrect) {
            navigator.navigateToPasswordResetScreen()
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
                text = "Email Verification ",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Beige
                ),
                modifier = Modifier.padding(top = 40.dp)

            )

            Text(
                text = "Remaining: ${formatTime(remainingTime)}",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Beige
                ),
                modifier = Modifier.padding(top = 40.dp)

            )

            OutlinedTextFieldWithLabelAndValidation(
                modifier = Modifier.padding(top = 10.dp),
                value = if (state.otp.toIntOrNull() == null
                ) "" else state.otp,
                onValueChange = {
                    if (it.length<=4) {
                        verificationModel.updateState(state.copy(otp = it))
                    }

                },
                placeHolder = {
                    Text(text = "Otp Number")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )
            HyperLinkClickable(
                text = "Didn't get any email? ",
                linkText = "Resend",
                linkColor = MaterialTheme.colorScheme.tertiary
            ) {
                verificationModel.resendOtp()
                Toast.makeText(context, "Resending Otp...", Toast.LENGTH_SHORT).show()
            }

            ConfirmButton(text = "Submit", onClick = {
                    verificationModel.verifyOtp()
            }, isEnabled = state.otp.isNotEmpty(), testTag = "")

            Button(
                onClick = {
                          verificationModel.cancel()
                    navigator.navigateToLoginScreen()
                },
                modifier = Modifier
                    .width(150.dp)
                    .height(70.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Red
                ),
                shape = RoundedCornerShape(25.dp),
            ) {
                Text(
                    text = "Cancel",
                    fontWeight = FontWeight.Bold
                )
            }
        }
        if (state.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center),color = MaterialTheme.colorScheme.tertiary)
        }
    }


}

private fun formatTime(millis: Long): String {
    val seconds = millis / 1000
    val minutes = seconds / 60
    val formattedSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, formattedSeconds)
}