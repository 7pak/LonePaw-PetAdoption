package com.auth.ui.common_components

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.auth.ui.screens.signup.Country
import com.auth.ui.screens.signup.getFlagEmojiFor


interface AuthScreenNavigator {
    fun navigateToLoginScreen()
    fun navigateToSignUpScreen()
    fun navigateToHomeScreen()
    fun navigateToEmailVerificationScreen()
    fun navigateToOtpVerificationScreen()
    fun navigateToPasswordResetScreen()
}

data class CombineKeys(
    val key1: String,
    val key2: String? = null,
    val key3: String? = null,
    val key4: String? = null,
    val key5: String? = null
)


@Composable
fun BackHandler(
    enabled: Boolean = true,
    onBackPressed: () -> Unit,
    dispatcher: OnBackPressedDispatcher
) {
    val callback = rememberUpdatedState(onBackPressed)

    DisposableEffect(callback) {
        val backCallback = object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                callback.value()
            }
        }

        dispatcher.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldWithLabelAndValidation(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
  //  label: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    placeHolder: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    testTag: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(25.dp),
        modifier = modifier
            .testTag(testTag)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(25.dp)
            ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.White,
            focusedBorderColor = MaterialTheme.colorScheme.tertiary,
            errorContainerColor = Color.White,
            cursorColor = MaterialTheme.colorScheme.tertiary
        ),
        placeholder = placeHolder,
        trailingIcon = trailingIcon,
        leadingIcon = leadingIcon,
        isError = isError,
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
fun ConfirmButton(
    modifier: Modifier = Modifier,
    text:String,
    isEnabled: Boolean,
    testTag: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.tertiary.copy(0.3f),
        ),
        modifier = modifier
            .height(55.dp)
            .testTag(testTag),
        enabled = isEnabled
    ) {
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun HyperLinkClickable(
    modifier: Modifier = Modifier,
    text: String,
    linkText: String,
    linkColor: Color,
    clickableText: () -> Unit
) {
    val tag = "Link"
    val annotatedString = buildAnnotatedString {
        withStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        ) {
            append(text)
        }
        pushStringAnnotation(tag = tag, annotation = linkText)
        withStyle(
            style = SpanStyle(
                color = linkColor,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        ) {
            append(linkText)
        }
        pop()
    }

    ClickableText(
        modifier = modifier,
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset).firstOrNull()?.let { annotation ->
                if (annotation.tag == tag) {
                    clickableText()
                }
            }
        }
    )
}


@Composable
fun CountryCodePickerDialog(
    countries: List<Country>,
    onSelection: (Country) -> Unit,
    dismiss: () -> Unit,
) {
    Dialog(onDismissRequest = dismiss) {
        Box {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 40.dp)
                    .background(shape = RoundedCornerShape(20.dp), color = Color.White)
            ) {
                countries.forEach{country->
                    item {
                        Text(
                            modifier = Modifier
                                .clickable {
                                    onSelection(country)
                                    dismiss()
                                }
                                .fillMaxWidth()
                                .padding(10.dp),
                            text = "${getFlagEmojiFor(country.nameCode)} ${country.fullName}"
                        )
                    }
                }
            }
        }
    }
}

fun isUsernameValid(username: String): Boolean {
    val specialCharsRegex = "[!@#\$%^&*(),.?\":{}|<>]".toRegex()
    return username.isNotEmpty() && !specialCharsRegex.containsMatchIn(username) && username.length >= 3
}

fun isEmailValid(email: String): Boolean {
    val emailRegex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
    return emailRegex.matches(email)
}

fun isPasswordValid(password: String,inLogin:Boolean = false): Boolean {
    val uppercaseRegex = "[A-Z]".toRegex()
    val lowercaseRegex = "[a-z]".toRegex()
    val digitRegex = "\\d".toRegex()
    val specialCharsRegex = "[!@#\$%^&*(),.?\":{}|<>]".toRegex()

    val containsUppercase = uppercaseRegex.containsMatchIn(password)
    val containsLowercase = lowercaseRegex.containsMatchIn(password)
    val containsDigit = digitRegex.containsMatchIn(password)
    val containSpecialChar = specialCharsRegex.containsMatchIn(password)

    if (inLogin) return  password.isNotEmpty() && password.length >= 8

    return password.isNotEmpty() && containsUppercase && containsLowercase && password.length >= 8 && containsDigit && containSpecialChar
}


