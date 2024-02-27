package com.home.ui.screens.home.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.core.common.ui.theme.Beige
import com.home.ui.screens.home.HomeModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchItem(
    modifier: Modifier = Modifier,
    homeModel: HomeModel,
    onSearchPressed: () -> Unit
) {
    val state = homeModel.state


    val focusedManager = LocalFocusManager.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = {
                homeModel.updateState(state.copy(searchQuery = it))
            },
            modifier = Modifier
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(15.dp),
                )
                .height(55.dp)
                .width(260.dp),
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Beige,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.tertiary,
            ),
            placeholder = {
                Text(
                    text = "Search for pet...",
                    color = MaterialTheme.colorScheme.tertiary.copy(0.3f)
                )
            },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                if (state.searchQuery.isNotEmpty()) {
                    onSearchPressed()
                }
                focusedManager.clearFocus(true)
            }
            )
        )

        Spacer(modifier = Modifier.width(20.dp))

        Box(
            modifier = Modifier
                .size(55.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.tertiary), contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {
                    onSearchPressed()
                    focusedManager.clearFocus(true)
                },
                enabled = state.searchQuery.isNotEmpty(),
                colors = IconButtonDefaults.iconButtonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary.copy(0.2f),
                    containerColor = MaterialTheme.colorScheme.tertiary
                ),
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }
        }
    }
}


