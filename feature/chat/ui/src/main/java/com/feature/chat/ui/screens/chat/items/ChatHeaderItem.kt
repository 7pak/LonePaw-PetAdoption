package com.feature.chat.ui.screens.chat.items

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.core.common.R
import com.core.common.ui.theme.Beige
import com.feature.chat.ui.ui.theme.PetAdoptionTheme

@Composable
fun ChatHeaderItem(modifier: Modifier = Modifier, context: Context,profilePic:String,profileName:String) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(horizontal = 2.dp, vertical = 2.dp)
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context = context)
                .data(
                    if (profilePic.isNullOrEmpty()) R.drawable.ic_profile else profilePic
                )
                .build(),
            placeholder = painterResource(id = R.drawable.ic_profile)
        )

        Image(
            painter = painter,
            contentDescription = "chat image",
            modifier = Modifier
                .size(50.dp)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop)


        Text(
            text = profileName,
            style = MaterialTheme.typography.titleMedium.copy(color = Beige,fontWeight = FontWeight.Medium)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatHeaderItemPreview() {
    PetAdoptionTheme {
       // ChatHeaderItem(context = LocalContext.current)
    }
}