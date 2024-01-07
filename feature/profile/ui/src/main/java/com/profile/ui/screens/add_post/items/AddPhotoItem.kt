package com.profile.ui.screens.add_post.items

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp

@Composable
fun AddPhotoItem(onAddedPhoto: (Uri?) -> Unit) {

    var uri: Uri? by remember {
        mutableStateOf(null)
    }

    // Handle the result from the photoPicker
    val photoPickerResultHandler: (Uri?) -> Unit = { newUri ->
        Log.d("AddPet", "AddPhotoItem: $uri")
        if (newUri != null) {
            uri = newUri
        }
    }

    LaunchedEffect(key1 = uri){
        onAddedPhoto(uri)
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = photoPickerResultHandler
    )
    Row(
        Modifier
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(30.dp)
            )
            .padding(horizontal = 15.dp)
            .clickable {
                photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                if (uri != null) {
                    onAddedPhoto(uri)
                }

            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Attach photo",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = if (uri == null) Color.Black else MaterialTheme.colorScheme.tertiary,
                fontStyle = FontStyle.Italic
            )
        )

        Icon(
            imageVector = if (uri == null) Icons.Default.AttachFile else Icons.Default.Check,
            contentDescription = "attachment",
            tint = if (uri == null) Color.Black else MaterialTheme.colorScheme.tertiary
        )
    }
}