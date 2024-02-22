package com.feature.chat.ui.screens.chat.items

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.core.common.ui.theme.Beige
import com.feature.chat.domain.model.ImageMessage
import com.feature.chat.ui.screens.chat.ChatViewModel
import com.feature.chat.ui.ui.theme.PetAdoptionTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SendMessageItem(
    modifier: Modifier = Modifier,
    context:Context,
    chatViewModel: ChatViewModel,
    onSend: () -> Unit
) {

    val state = chatViewModel.state

    var currentPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "com.abdts.petadoption.provider", file
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                chatViewModel.updateState(state.copy(message = uri.toString(), type = ImageMessage.IMAGE))
                onSend()
            }
        }
    )
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA,
        onPermissionResult = { granted ->
            if (granted) {
                cameraLauncher.launch(uri)
            } else Toast.makeText(context, "camera permission denied", Toast.LENGTH_SHORT).show()
        }
    )


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {

        IconButton(onClick = {
            cameraPermissionState.launchPermissionRequest()
        },modifier = Modifier) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "camera",
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.fillMaxSize()
            )
        }

        OutlinedTextField(
            value = state.message,
            onValueChange = {
                chatViewModel.updateState(state.copy(message = it))
            },
            modifier = Modifier
                .weight(9f)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                ),
            shape = CircleShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Beige,
                unfocusedContainerColor = Beige,
                disabledContainerColor = Beige,
                focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            ),
            placeholder = {
                Text(
                    text = "Message...",
                    color = Color.Black.copy(0.3f)
                )
            },
            trailingIcon = {
                if (state.message.isNotEmpty()) {
                    IconButton(
                        onClick = onSend, modifier = Modifier
                            .weight(1f)
                            .size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "send message",
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        )

    }
}
@RequiresApi(Build.VERSION_CODES.FROYO)
fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir /* directory */
    )
}

@Preview(showBackground = true)
@Composable
fun SendMessageItemPreview() {
    PetAdoptionTheme {
        //SendMessageItem()
    }
}