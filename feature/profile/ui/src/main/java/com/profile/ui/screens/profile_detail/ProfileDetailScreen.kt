package com.profile.ui.screens.profile_detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.common.R
import com.profile.ui.screens.profile_detail.items.ConfirmProfileButtonItem
import com.profile.ui.screens.profile_detail.items.EditImageItem
import com.profile.ui.screens.profile_detail.items.OutLineTextFieldItem
import com.profile.ui.ui.theme.PetAdoptionTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ProfileDetailScreen(
    profileDetailModel: ProfileDetailModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {

    val state = profileDetailModel.state

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {
                EditImageItem(context = context) {

                }
            }

            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutLineTextFieldItem(value = state.name, onValueChange = {
                        profileDetailModel.updateProfileStete(state.copy(name = it))
                    }, label = "Name", focusManager = focusManager, modifier = Modifier.weight(5f))

                    Spacer(
                        modifier = Modifier
                            .width(5.dp)
                            .weight(0.5f)
                    )

                    OutLineTextFieldItem(
                        value = state.username,
                        onValueChange = {
                            profileDetailModel.updateProfileStete(state.copy(username = it))
                        },
                        label = "Username",
                        focusManager = focusManager,
                        modifier = Modifier.weight(5f)
                    )
                }
            }

            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutLineTextFieldItem(
                        value = state.country,
                        onValueChange = {
                            profileDetailModel.updateProfileStete(state.copy(country = it))
                        },
                        label = "Country",
                        focusManager = focusManager,
                        modifier = Modifier.weight(5f)
                    )

                    Spacer(
                        modifier = Modifier
                            .width(5.dp)
                            .weight(0.5f)
                    )


                    OutLineTextFieldItem(
                        value = state.contactNumber,
                        onValueChange = {
                            profileDetailModel.updateProfileStete(state.copy(contactNumber = it))
                        },
                        label = "Contact number",
                        focusManager = focusManager,
                        modifier = Modifier.weight(5f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
            }

            item {
                OutLineTextFieldItem(
                    value = state.email,
                    onValueChange = {
                        profileDetailModel.updateProfileStete(state.copy(email = it))
                    },
                    label = "Email",
                    focusManager = focusManager,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(horizontal = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
            }



            item {
                OutLineTextFieldItem(
                    value = state.address,
                    onValueChange = {
                        profileDetailModel.updateProfileStete(state.copy(address = it))
                    },
                    label = "Address",
                    focusManager = focusManager,
                    maxLines = 2,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(horizontal = 8.dp)
                )
            }

            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .clickable {

                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Reset password",
                        style = MaterialTheme.typography.bodyLarge.copy(),
                        modifier = Modifier
                    )
                    Icon(
                        imageVector = Icons.Default.NavigateNext,
                        contentDescription = "navigate_password",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier
                            .size(50.dp)
                    )
                }
            }


            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    ConfirmProfileButtonItem(icon = Icons.Default.Done, isEnabled = true) {

                        profileDetailModel.updateProfile()
                    }
                }
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun ProfileDetailScreenPreview() {
    PetAdoptionTheme {
        //  ProfileDetailScreen()
    }
}