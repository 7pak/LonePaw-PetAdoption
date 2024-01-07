package com.auth.ui.common_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.core.common.ui.theme.Red


interface AuthScreenNavigator {
    fun navigateToLoginScreen()
    fun navigateToSignUpScreen()
    fun navigateToHomeScreen()
}

data class CombineKeys(val key1:String,val key2: String?=null,val key3: String?=null,val key4: String?=null,val key5: String?=null)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithLabelAndValidation(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeHolder: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    testTag: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth(0.6f)
            .padding(horizontal = 15.dp)
            .padding(vertical = 10.dp)
            .testTag(testTag),
        label = {
            Text(
                text = label
            )
        },
        placeholder = placeHolder
        ,
        colors = TextFieldDefaults.textFieldColors(
          containerColor = Color.White,
            focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
            focusedLabelColor = MaterialTheme.colorScheme.tertiary,
            textColor = Color.Black,
            cursorColor = MaterialTheme.colorScheme.tertiary
        ),
        trailingIcon = trailingIcon,
        isError = isError,
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )

//    if (isError) {
//        Text(
//            text = errorMessage,
//            fontSize = 14.sp,
//            color = Color.Red,
//            modifier = Modifier.padding(8.dp)
//        )
//    }
}

@Composable
fun ConfirmButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    isEnabled: Boolean,
    testTag: String,
    onClick: () -> Unit,
)
{
   Button(onClick = onClick, colors = ButtonDefaults.buttonColors(
       containerColor = MaterialTheme.colorScheme.tertiary,
       contentColor = Color.White,
       disabledContainerColor = Color.Gray
   ),modifier = Modifier.size(80.dp).clip(shape = CircleShape).testTag(testTag), enabled = isEnabled) {
       Icon(imageVector = icon, contentDescription = "CONFIRM",modifier = Modifier.size(50.dp))
   }
}

@Composable
fun CoupleDots(){
    Column(
        Modifier.padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(25.dp)
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.tertiary)
        )
        Box(
            modifier = Modifier
                .size(15.dp)
                .clip(shape = CircleShape)
                .background(Color.Black)
        )
    }
}

fun isUsernameValid(username: String): Boolean {
    val specialCharsRegex = "[!@#\$%^&*(),.?\":{}|<>]".toRegex()
    return username.isNotEmpty()&& !specialCharsRegex.containsMatchIn(username) && username.length >= 3
}

fun isEmailValid(email: String): Boolean {
    val emailRegex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
    return emailRegex.matches(email)
}

fun isPasswordValid(password: String): Boolean {
    val letterRegex = "[a-zA-Z]".toRegex()
    val digitRegex = "\\d".toRegex()
    val specialCharsRegex = "[!@#\$%^&*(),.?\":{}|<>]".toRegex()

    val containsLetter = letterRegex.containsMatchIn(password)
    val containsDigit = digitRegex.containsMatchIn(password)
    val containSpecialChar = specialCharsRegex.containsMatchIn(password)

    return password.isNotEmpty() && password.length >= 8 && containsDigit && containsLetter && containSpecialChar
}
