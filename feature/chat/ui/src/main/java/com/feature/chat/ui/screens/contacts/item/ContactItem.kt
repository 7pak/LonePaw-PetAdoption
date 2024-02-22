package com.feature.chat.ui.screens.contacts.item

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.core.common.R
import com.core.common.ui.theme.Beige
import com.feature.chat.domain.model.ChatContent
import com.feature.chat.domain.model.ImageMessage
import com.feature.chat.domain.model.MessageStatus
import com.feature.chat.domain.model.User
import com.feature.chat.ui.shared.formatDateString
import com.feature.chat.ui.ui.theme.PetAdoptionTheme
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun ContactItem(
    context: Context,
    user: User,
    chatContent: ChatContent,
    onDeleteChat: () -> Unit,
    onNavigate: () -> Unit
) {

    SwipeableActionsBox(

        startActions = swipeActionItem(
            icon = Icons.Default.Delete,
            color = Color.Red
        ) {
            onDeleteChat()
        }
    ) {
        Card(
            onClick = onNavigate, modifier = Modifier
                .padding(vertical = 15.dp)
                .height(80.dp)
                .padding(start = 5.dp, end = 20.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = CircleShape
                )
                .clip(CircleShape)
        ) {
            ContactData(context = context, user = user, chatContent = chatContent)
        }
    }
}

@Composable
fun ContactData(context: Context, user: User, chatContent: ChatContent) {

    val lastMessage = when (chatContent.type) {
        ImageMessage.IMAGE -> "photo"
        else -> {
            chatContent.message
        }
    }

    Row(
        Modifier
            .fillMaxSize()
            .background(Beige)
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context = context)
                .data(
                    user.profilePic.ifEmpty { R.drawable.ic_profile }
                )
                .build(),
            placeholder = painterResource(id = R.drawable.ic_profile)
        )

        Image(
            painter = painter,
            contentDescription = "contact image",
            modifier = Modifier
                .fillMaxHeight()
                .weight(1.4f)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop,
            colorFilter = if (user.profilePic.isNullOrEmpty())
                ColorFilter.tint(MaterialTheme.colorScheme.tertiary) else null
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 3.dp, vertical = 10.dp)
                .weight(3.3f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (user.id == chatContent.senderId && chatContent.messageStatus == MessageStatus.UNSEEN) {
                    Icon(
                        imageVector = Icons.Default.Circle,
                        contentDescription = "new message",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(10.dp)
                    )
                }

                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = if (user.id == chatContent.recipientId) "You: $lastMessage" else lastMessage,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (user.id == chatContent.senderId && chatContent.messageStatus == MessageStatus.UNSEEN) FontWeight.ExtraBold else FontWeight.Normal
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Text(
            text = formatDateString(chatContent.date),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
            modifier = Modifier
                .padding(5.dp)
                .weight(1.6f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis

        )
    }
}

fun swipeActionItem(icon: ImageVector, color: Color, onClick: () -> Unit): List<SwipeAction> {
    return listOf(
        SwipeAction(
            onSwipe = {
                onClick()
            },
            icon = {
                Icon(
                    modifier = Modifier.padding(10.dp),
                    imageVector = icon,
                    contentDescription = "SWIPE ACTION"
                )
            },
            background = color
        )
    )
}


@Preview(showBackground = true)
@Composable
fun ContactItemPreview() {
    PetAdoptionTheme {
        //ContactItem()
    }
}