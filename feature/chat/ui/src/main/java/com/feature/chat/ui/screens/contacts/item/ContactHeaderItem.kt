package com.feature.chat.ui.screens.contacts.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.common.ui.theme.Beige
import com.feature.chat.ui.screens.contacts.ContactViewModel
import com.feature.chat.ui.ui.theme.PetAdoptionTheme

@Composable
fun ContactHeaderItem(contactViewModel: ContactViewModel) {

    val focusedManager = LocalFocusManager.current

    val searchQuery by contactViewModel.searchQuery.collectAsState()

    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {

        Text(
            text = "Chats",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = Beige,
                fontFamily = FontFamily.Monospace
            )
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                contactViewModel.updateSearchQuery(it)
            },
            modifier = Modifier
                .height(55.dp)
                .fillMaxWidth()
                .padding(end = 10.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                ),
            shape = CircleShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Beige,
                unfocusedContainerColor = Beige,
                disabledContainerColor = Beige,
                focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            ),
            placeholder = {
                Text(
                    text = "Chat with...",
                    color = MaterialTheme.colorScheme.tertiary.copy(0.3f)
                )
            },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                focusedManager.clearFocus(true)
            }
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContactHeaderItemPreview() {
    PetAdoptionTheme {
       // ContactHeaderItem()
    }
}