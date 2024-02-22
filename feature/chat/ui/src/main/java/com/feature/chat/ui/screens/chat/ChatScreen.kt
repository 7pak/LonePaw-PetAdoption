package com.feature.chat.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.common.ui.theme.Beige
import com.feature.chat.ui.screens.chat.items.ChatHeaderItem
import com.feature.chat.ui.screens.chat.items.MessageItem
import com.feature.chat.ui.screens.chat.items.SendMessageItem
import com.feature.chat.ui.shared.ChatNavArgs
import com.feature.chat.ui.ui.theme.PetAdoptionTheme
import com.ramcosta.composedestinations.BuildConfig
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@Destination(navArgsDelegate = ChatNavArgs::class)
@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
    }

    val messagesList by chatViewModel.messagesListState.collectAsState(emptyList())
    val recipientProfilePic by chatViewModel.recipientProfilePic.collectAsState()
    val recipientName by chatViewModel.recipientName.collectAsState()


    ConstraintLayout(
        Modifier
            .wrapContentHeight()
            .fillMaxSize()
            .background(Beige)
    ) {

        val (chatHeader, chatList, sendMessage) = createRefs()
        ChatHeaderItem(
            modifier = Modifier.constrainAs(chatHeader) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints

            },
            context = context,
            profilePic =recipientProfilePic ,
            profileName = recipientName
        )



        LazyColumn(
            reverseLayout = true,
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(chatList) {
                    start.linkTo(parent.start)
                    top.linkTo(chatHeader.bottom)
                    end.linkTo(parent.end)
                    bottom.linkTo(sendMessage.top)

                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        ) {
            items(messagesList) {
                MessageItem(
                    currentUserId = chatViewModel.currentUserId.toString(),
                    chatContent = it,
                    context = context
                )
            }
        }
        SendMessageItem(
            modifier = Modifier.constrainAs(sendMessage) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            },
            context = context,
            chatViewModel = chatViewModel
        ) {
            chatViewModel.sendMessages()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    PetAdoptionTheme {
        ChatScreen()
    }
}