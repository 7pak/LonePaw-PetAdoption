package com.home.ui.screens.home.items

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ForwardToInbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.core.common.R
import com.core.common.ui.theme.Beige

@Composable
fun HeaderItem(
    context: Context,
    modifier: Modifier = Modifier,
    profilePic: String,
    profileName: String,
    profileCountry:String,
    hasUnseenMessage:Boolean,
    onChatNavigate: () -> Unit,
    onProfileNavigate: () -> Unit
) {

    Row(
        modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context = context)
                .data(
                    profilePic.ifEmpty { R.drawable.ic_profile}
                )
                .transformations(CircleCropTransformation())
                .build(),
            placeholder = painterResource(id = R.drawable.ic_profile)
        )

        Image(
            painter = painter,
            contentDescription = "profile_header",
            modifier = Modifier
                .size(70.dp)
                .clickable {
                    onProfileNavigate()
                }
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = profileName,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Beige,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = profileCountry,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Beige,
                    fontWeight = FontWeight.Light
                )
            )


        }

        Box(modifier = Modifier.size(35.dp)) {
            IconButton(onClick = onChatNavigate) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ForwardToInbox,
                    contentDescription = "inbox",
                    tint = Beige,
                    modifier = Modifier
                        .size(35.dp)
                )
            }

            if (hasUnseenMessage) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-6).dp)
                )
            }
        }
    }
}

