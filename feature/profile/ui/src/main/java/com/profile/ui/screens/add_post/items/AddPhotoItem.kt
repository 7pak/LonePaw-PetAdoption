package com.profile.ui.screens.add_post.items

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.core.common.R

@Composable
fun AddPhotoItem(context: Context, onAddedPhoto: (List<String>?) -> Unit) {


    val uris = remember {
        mutableStateListOf<String>()
    }

    var updateInProgress by remember { mutableStateOf(false) }
    var removedUri: String? by remember {
        mutableStateOf(null)
    }


    val photoPickerResultHandler: (Uri?) -> Unit = { newUriList ->

        if (updateInProgress) {
            if (newUriList != null) {
                uris[uris.indexOf(removedUri)] = newUriList.toString()
                removedUri = null
                onAddedPhoto(uris)
            }
            updateInProgress = false
        } else if (newUriList != null) {
            uris.add(newUriList.toString())
            onAddedPhoto(uris)
        }
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = photoPickerResultHandler
    )

    LazyRow(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(uris) { uri ->
            PhotoBox(context = context, uri = uri, onAdd = {}, onUpdate = { preUri ->
                photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

                removedUri = preUri
                updateInProgress = true

            }) { deletedUri ->
                uris.remove(deletedUri)
                onAddedPhoto(uris)
            }
        }
        item {
            if (uris.size < 5) {
                PhotoBox(context = context, onAdd = {
                    photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                })
            }
        }
    }
}

@Composable
fun PhotoBox(
    context: Context,
    uri: String? = null,
    onAdd: () -> Unit,
    onUpdate: (String) -> Unit = {},
    onDelete: (String) -> Unit = {}
) {

    Box(modifier = Modifier.size(85.dp)) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(75.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable {
                    if (!uri.isNullOrEmpty()) {
                        onUpdate(uri)
                    } else {
                        onAdd()
                    }
                },
            contentAlignment = Alignment.Center
        ) {

            if (uri.isNullOrEmpty()) {
                Icon(
                    imageVector = Icons.Sharp.Add,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            } else {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context = context)
                        .data(
                            uri
                        )
                        .build(),
                    placeholder = painterResource(id = R.drawable.ic_pet)
                )
                Image(
                    painter = painter,
                    contentDescription = "add pet photos",
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }

        if (!uri.isNullOrEmpty()) {
            IconButton(
                onClick = {
                    if (!uri.isNullOrEmpty()) {
                        onDelete(uri)
                    }
                },
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Sharp.RemoveCircle,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
