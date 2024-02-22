package com.profile.ui.screens.add_post.items

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenuEditableItem(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    items: Map<Int, String>,
    onItemSelected: (String) -> Unit,
    placeHolder: String,
    onDismissRequest: () -> Unit
) {
    var selectedText by remember { mutableStateOf(placeHolder) }

    Box(
        modifier = modifier
            .fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange
        ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(),
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                    //unfocusedBorderColor = MaterialTheme.colorScheme.tertiary,
                  //  textColor = MaterialTheme.colorScheme.tertiary
                ),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest
            ) {
                items.entries.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(text = item.value)
                               },
                        onClick = {
                            selectedText = item.value
                            onItemSelected(item.value)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}

