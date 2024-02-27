package com.profile.ui.screens.profile_detail.items

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OutLineTextFieldItem(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeHolder: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
    testTag: String = "",
    roundedCornerDp: Dp = 30.dp,
    singleLine: Boolean = true,
    readOnly:Boolean = false,
    maxLines: Int = 1,
    focusManager: FocusManager,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    keyboardActions: KeyboardActions = KeyboardActions(onNext = {
        focusManager.moveFocus(FocusDirection.Down)
    }, onDone = { focusManager.clearFocus(true) })
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .padding(vertical = 7.dp)
            .testTag(testTag),
        shape = RoundedCornerShape(roundedCornerDp),
        label = {
            Text(
                text = label
            )
        },
        placeholder = placeHolder,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.White,
            focusedLabelColor = MaterialTheme.colorScheme.tertiary,
            //textColor = Color.Black,
            cursorColor = MaterialTheme.colorScheme.tertiary,
            focusedBorderColor = MaterialTheme.colorScheme.tertiary
        ),
        trailingIcon = trailingIcon,
        isError = isError,
        singleLine = singleLine,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        readOnly = readOnly
    )
}

@Composable
internal fun ConfirmProfileButtonItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    isEnabled: Boolean,
    testTag: String="",
    onClick: () -> Unit,
)
{
    OutlinedButton(onClick = onClick, colors = ButtonDefaults.outlinedButtonColors(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = Color.White,
        disabledContainerColor = Color.Gray
    ),modifier = modifier.size(75.dp).clip(shape = CircleShape).testTag(testTag), enabled = isEnabled) {
        Icon(imageVector = icon, contentDescription = "CONFIRM",modifier = Modifier.size(50.dp))
    }
}