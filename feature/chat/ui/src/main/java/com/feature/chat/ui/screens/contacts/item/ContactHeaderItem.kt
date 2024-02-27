package com.feature.chat.ui.screens.contacts.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.core.common.ui.theme.Beige
import com.feature.chat.ui.ui.theme.PetAdoptionTheme

@Composable
fun ContactHeaderItem() {


    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        Text(
            text = "Chats",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = Beige,
                fontFamily = FontFamily.Monospace
            ),
            modifier = Modifier.padding(top = 20.dp)
        )

        HorizontalDivider(
            modifier = Modifier.height(9.dp).padding(vertical = 5.dp).padding(bottom = 25.dp),
            color = Beige
        )

//        OutlinedTextField(
//            value = searchQuery,
//            onValueChange = {
//                contactViewModel.updateSearchQuery(it)
//            },
//            modifier = Modifier
//                .height(55.dp)
//                .fillMaxWidth()
//                .padding(end = 10.dp)
//                .shadow(
//                    elevation = 8.dp,
//                    shape = CircleShape,
//                ),
//            shape = CircleShape,
//            colors = OutlinedTextFieldDefaults.colors(
//                focusedContainerColor = Beige,
//                unfocusedContainerColor = Beige,
//                disabledContainerColor = Beige,
//                focusedBorderColor = MaterialTheme.colorScheme.tertiary,
//                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
//            ),
//            placeholder = {
//                Text(
//                    text = "Chat with...",
//                    color = MaterialTheme.colorScheme.tertiary.copy(0.3f)
//                )
//            },
//            singleLine = true,
//            maxLines = 1,
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//            keyboardActions = KeyboardActions(onSearch = {
//                focusedManager.clearFocus(true)
//            }
//            )
//        )
    }
}

@Preview(showBackground = true)
@Composable
fun ContactHeaderItemPreview() {
    PetAdoptionTheme {
        // ContactHeaderItem()
    }
}