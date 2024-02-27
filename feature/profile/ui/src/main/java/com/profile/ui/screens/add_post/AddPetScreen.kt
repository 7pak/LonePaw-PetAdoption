package com.profile.ui.screens.add_post

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.common.R
import com.profile.ui.screens.add_post.items.AddPhotoItem
import com.profile.ui.screens.add_post.items.ConfirmButtonItem
import com.profile.ui.screens.add_post.items.DropDownMenuEditableItem
import com.profile.ui.screens.add_post.items.OutLinedTextFieldItem
import com.profile.ui.shared.AddPostNavArgs
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(navArgsDelegate = AddPostNavArgs::class)
@Composable
fun AddPetScreen(addPostModel: AddPostModel = hiltViewModel(), navigator: DestinationsNavigator) {

    val state = addPostModel.state
    val onPostAdded by addPostModel.onPostAdded.collectAsState(false)


    val context = LocalContext.current
    val focusManager = LocalFocusManager.current



    DisposableEffect(key1 = onPostAdded) {
        if (onPostAdded) {
            navigator.navigateUp()
        }

        onDispose {
            addPostModel.resetOnPostAdded()
        }
    }



    LaunchedEffect(key1 = true) {
        if (addPostModel.postId != -1) {
            addPostModel.getPetInfo(addPostModel.postId)
        }
    }


    var expanded by remember {
        mutableStateOf(false)
    }
    val categoryPets =
        mapOf(1 to "cat", 2 to "dog", 3 to "bird", 4 to "hamster", 5 to "rabbit", 6 to "other")

    val preCategory = categoryPets.entries.find { it.key == state.categoryId }?.value ?: ""

    var categoryItem by remember {
        mutableStateOf(preCategory)
    }

    addPostModel.updateState(
        state.copy(
            categoryId = try {
                categoryPets.entries.find { it.value == categoryItem }!!.key

            } catch (e: NullPointerException) {
                0
            }
        )
    )
    var isFormFilled by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(state) {
        isFormFilled =
            state.petName.isNotEmpty() && state.petType.isNotEmpty() &&
                    state.petAge != null && !state.petPhoto.isNullOrEmpty() && state.petGender.isNotEmpty() &&
                    state.petBreed.isNotEmpty() && state.petDesc.isNotEmpty() && state.categoryId != null
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_post),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .background(MaterialTheme.colorScheme.background)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Text(
                    text = "Donate a pet",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        fontFamily = FontFamily.Monospace
                    ),
                    modifier = Modifier.padding(top = 10.dp)
                )


                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                OutLinedTextFieldItem(
                    value = state.petName, onValueChange = {
                        addPostModel.updateState(state.copy(petName = it))
                    }, label = "Pet name",
                    focusManager = focusManager
                )

                OutLinedTextFieldItem(
                    value = state.petType, onValueChange = {
                        addPostModel.updateState(state.copy(petType = it))
                    }, label = "Pet type",
                    focusManager = focusManager
                )

                OutLinedTextFieldItem(
                    value = if (state.petAge == null) "" else state.petAge.toString(),
                    onValueChange = { newText ->
                        if (newText.isEmpty()) {
                            // If the input is empty, set petAge to null
                            addPostModel.updateState(state.copy(petAge = null))
                        } else if (newText.matches("\\d+".toRegex()) && newText.length <= 3) {
                            // Check if input consists only of digits and has a length of 3 or less
                            addPostModel.updateState(state.copy(petAge = newText.toInt()))
                        }

                    },
                    label = "Pet age",
                    focusManager = focusManager,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutLinedTextFieldItem(
                    value = state.petBreed, onValueChange = {
                        addPostModel.updateState(state.copy(petBreed = it))
                    }, label = "Pet breed",
                    focusManager = focusManager
                )

                OutLinedTextFieldItem(
                    value = state.petGender, onValueChange = {
                        addPostModel.updateState(state.copy(petGender = it))
                    }, label = "Pet gender",
                    focusManager = focusManager
                )

                OutLinedTextFieldItem(
                    value = state.petDesc, onValueChange = {
                        if (it.length < 200) {
                            addPostModel.updateState(state.copy(petDesc = it))
                        }
                    }, label = "Pet Description",
                    focusManager = focusManager,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier.height(120.dp),
                    roundedCornerDp = 10.dp,
                    singleLine = false,
                    maxLines = 3
                )
            }

            item {
                DropDownMenuEditableItem(
                    placeHolder = preCategory.ifEmpty { "Category" },
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    items = categoryPets,
                    onItemSelected = {
                        categoryItem = it
                    },
                    onExpandedChange = {
                        expanded = it
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                AddPhotoItem {uriList->
                    uriList?.let {
                        addPostModel.updateState(state.copy(petPhoto =uriList))
                    }
                }
            }

            item {
                ConfirmButtonItem(
                    icon = Icons.AutoMirrored.Filled.ArrowForward,
                    isEnabled = isFormFilled
                ) {

                    if (addPostModel.postId != -1) {
                        addPostModel.updatePost()
                    } else {
                        addPostModel.addPost()
                    }
                }

                Spacer(modifier = Modifier.height(7.dp))
            }
        }
        if (state.isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}