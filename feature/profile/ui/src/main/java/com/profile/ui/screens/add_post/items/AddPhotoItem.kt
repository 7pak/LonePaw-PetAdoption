package com.profile.ui.screens.add_post.items

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
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
fun AddPhotoItem(onAddedPhoto: (List<String>?) -> Unit) {

    var uris by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    val photoPickerResultHandler: (List<Uri>?) -> Unit = { newUriList ->
        Log.d("AddPet", "AddPhotoItem: $uris")
        if (!newUriList.isNullOrEmpty()) {
            uris = newUriList.map { it.toString() }
        }
    }

    LaunchedEffect(key1 = uris) {
        onAddedPhoto(uris)
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems =5 ),
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
                if (uris.isNotEmpty()) {
                    onAddedPhoto(uris)
                }

            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Attach photo",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = if (uris.isEmpty()) Color.Black else MaterialTheme.colorScheme.tertiary,
                fontStyle = FontStyle.Italic
            )
        )

        Icon(
            imageVector = if (uris.isEmpty()) Icons.Default.AttachFile else Icons.Default.Check,
            contentDescription = "attachment",
            tint = if (uris.isEmpty()) Color.Black else MaterialTheme.colorScheme.tertiary
        )
    }
}