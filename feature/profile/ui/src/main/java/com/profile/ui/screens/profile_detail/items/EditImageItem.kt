package com.profile.ui.screens.profile_detail.items

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.core.common.R

@Composable
internal fun EditImageItem(
    modifier: Modifier = Modifier,
    context: Context,
    image:String?,
    onClick: (Uri?) -> Unit
) {

    var uri: Uri? by remember {
        mutableStateOf(null)
    }

    val photoPickerResultHandler: (Uri?) -> Unit = { newUri ->
        Log.d("AddPet", "editPhoto: $uri")
        if (newUri != null) {
            uri = newUri
        }
    }

    LaunchedEffect(key1 = uri) {
        onClick(uri)
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = photoPickerResultHandler
    )

    Box(modifier = modifier
        .fillMaxWidth()
        .padding(10.dp)) {

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context = context)
                .data(
                    image?:R.drawable.ic_profile
                )
                .transformations(CircleCropTransformation())
                .build(),
            placeholder = painterResource(id = R.drawable.ic_profile)
        )

        Image(
            painter = painter,
            contentDescription = "profile_header_edit",
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.Center)
                .padding(top = 30.dp)
                .clickable {
                    photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    if (uri != null) {
                        onClick(uri)
                    }
                }
        )
    }
}