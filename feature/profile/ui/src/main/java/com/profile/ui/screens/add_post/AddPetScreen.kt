package com.profile.ui.screens.add_post

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.profile.ui.screens.add_post.items.AddPhotoItem
import com.profile.ui.screens.add_post.items.ConfirmButtonItem
import com.profile.ui.screens.add_post.items.DropDownMenuEditableItem
import com.profile.ui.screens.add_post.items.OutLinedTextFieldItem
import com.profile.ui.ui.theme.PetAdoptionTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.core.common.R
import com.profile.ui.shared.AddPostNavArgs

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

    LaunchedEffect(key1 = true){
        if (addPostModel.postId != -1){
            addPostModel.getPetInfo(addPostModel.postId)
        }
    }


    var expanded by remember {
        mutableStateOf(false)
    }
    val categoryCourses =
        mapOf(1 to "cat", 2 to "dog", 3 to "bird", 4 to "hamster", 5 to "rabbit",6 to "other")

    var categoryItem by remember {
        mutableStateOf("")
    }

    addPostModel.updateState(
        state.copy(
            categoryId = try {
                categoryCourses.entries.find { it.value == categoryItem }!!.key

            } catch (e: NullPointerException) {
                0
            }
        )
    )



    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(id = R.drawable.background_post),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
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
                    placeHolder = "Category",
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    items = categoryCourses,
                    onItemSelected = {
                        categoryItem = it
                    },
                    onExpandedChange = {
                        expanded = it
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                AddPhotoItem {
                    addPostModel.updateState(state.copy(petPhoto = it))
                    Log.d("AddPet", "AddPetScreen: $state----------------")
                }
            }

            item {
                ConfirmButtonItem(icon = Icons.Default.ArrowForward, isEnabled = true) {

                    if (addPostModel.postId != -1){
                        addPostModel.updatePost()
                    }else {
                        addPostModel.addPost()
                    }
                }

                Spacer(modifier = Modifier.height(7.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddPetScreenPreview() {
    PetAdoptionTheme {
        //     AddPetScreen()
    }
}