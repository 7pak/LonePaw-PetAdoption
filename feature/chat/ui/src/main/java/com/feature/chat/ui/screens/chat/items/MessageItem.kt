package com.feature.chat.ui.screens.chat.items

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.core.common.R
import com.core.common.ui.theme.Beige
import com.feature.chat.domain.model.ChatContent
import com.feature.chat.domain.model.ImageMessage
import com.feature.chat.domain.model.MessageType
import com.feature.chat.domain.model.TextMessage
import com.feature.chat.ui.shared.formatDateString
import com.feature.chat.ui.ui.theme.PetAdoptionTheme
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date

@Composable
fun MessageItem(currentUserId: String, chatContent: ChatContent,context: Context) {

    var messageType: MessageType by remember {
        mutableStateOf(TextMessage)
    }

    messageType = if (chatContent.type == ImageMessage.IMAGE) {
        ImageMessage
    } else TextMessage

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {

        if (currentUserId == chatContent.senderId) {
            MessageSentItem(
                modifier = Modifier.align(Alignment.CenterEnd),
                message = chatContent.message,
                date = chatContent.date,
                type = messageType,
                context = context
            )
        } else {
            MessageReceivedItem(
                modifier = Modifier.align(Alignment.CenterStart),
                message = chatContent.message,
                date = chatContent.date,
                type = messageType,
                context = context
            )
        }
    }
}


@Composable
fun MessageSentItem(
    modifier: Modifier = Modifier,
    context: Context,
    message: String,
    date: Date?,
    type: MessageType
) {
    Box(
        modifier = modifier
            .padding(horizontal = 6.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(12.dp), contentAlignment = Alignment.Center
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (type == ImageMessage) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context = context)
                        .data(
                            message
                        )
                        .build(),
                    placeholder = painterResource(id = R.drawable.ic_pet)
                )

                Image(
                    painter = painter,
                    contentDescription = "chat image",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .scale(1f)
                )
            } else {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(0.9f)
                    )
                )
            }


            Text(
                text = formatDateString(date),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic
                ),
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}

@Composable
fun MessageReceivedItem(
    modifier: Modifier = Modifier,
    message: String,
    date: Date?,
    type: MessageType,
    context: Context
) {
    Box(
        modifier = modifier
            .padding(horizontal = 6.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp),
            )
            .clip(shape = RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp))
            .background(Beige)
            .padding(12.dp),
        contentAlignment = Alignment.Center

    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (type == ImageMessage) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context = context)
                        .data(
                            message
                        )
                        .build(),
                    placeholder = painterResource(id = R.drawable.ic_pet)
                )

                Image(
                    painter = painter,
                    contentDescription = "chat image",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .scale(1f)
                )
            } else {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                )
            }

            Text(
                text = formatDateString(date),
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic
                ),
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageItemPreview() {
    PetAdoptionTheme {
        //MessageItem()
    }
}