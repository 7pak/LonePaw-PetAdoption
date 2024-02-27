package com.feature.chat.ui.screens.contacts

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.common.R
import com.core.common.ui.theme.Beige
import com.feature.chat.ui.screens.contacts.item.ContactHeaderItem
import com.feature.chat.ui.screens.contacts.item.ContactItem
import com.feature.chat.ui.shared.ContactScreenNavigator
import com.feature.chat.ui.ui.theme.PetAdoptionTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph


@RootNavGraph(start = true)
@Destination
@Composable
fun ContactsScreen(
    contactViewModel: ContactViewModel = hiltViewModel(),
    navigator: ContactScreenNavigator
) {

    LaunchedEffect(true) {
        contactViewModel.getContacts()
    }

    var state by remember {
        mutableStateOf(ContactStatus())
    }

    LaunchedEffect(key1 = true, key2 = contactViewModel.state.users) {
        state = contactViewModel.state
    }

    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        Image(
            painter = painterResource(id = R.drawable.background_post),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )


        LazyColumn(Modifier.fillMaxSize()) {
            item {
                ContactHeaderItem()
                Spacer(modifier = Modifier.height(20.dp))
            }

            if (state.users.isEmpty()) {
                item {
                    Text(
                        text = "there in no chats...",
                        style = MaterialTheme.typography.titleLarge.copy(color = Beige),
                        modifier = Modifier.padding(30.dp)
                    )
                }
            } else {
                items(state.users) { pair ->

                    val user = pair.first
                    val chatContent = pair.second

                    ContactItem(
                        context = context,
                        user = user,
                        chatContent = chatContent,
                        onDeleteChat = {
                            user.id?.let {
                                contactViewModel.deleteChat(it)
                            }
                            Toast.makeText(context, "${user.name} removed", Toast.LENGTH_SHORT)
                                .show()
                        }) {
                        user.id?.toIntOrNull()?.let {
                            navigator.navigateToChatScreen(it)
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Swipe right to remove contact..",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.tertiary,
                                fontWeight = FontWeight.Light,
                                fontStyle = FontStyle.Italic
                            ),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactItemPreview() {
    PetAdoptionTheme {
        //ContactsScreen()
    }
}