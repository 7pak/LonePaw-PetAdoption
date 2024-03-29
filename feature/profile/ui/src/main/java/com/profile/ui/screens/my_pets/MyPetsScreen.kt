package com.profile.ui.screens.my_pets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.common.test_tags.HomeTestTags
import com.profile.ui.screens.my_pets.items.MyPetHeaderItem
import com.profile.ui.screens.my_pets.items.MyPetItem
import com.profile.ui.shared.ProfileScreenNavigator
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun MyPetsScreen(myPetsModel: MyPetsModel = hiltViewModel(), navigator: ProfileScreenNavigator) {
    val context = LocalContext.current

    val state = myPetsModel.state
    LaunchedEffect(key1 = true) {
        myPetsModel.getMyPets()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .testTag(HomeTestTags.HOME_SCREEN),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        MyPetHeaderItem {
            navigator.navigateToAddPostScreen()
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(MaterialTheme.colorScheme.primary),
            ) {

                if (state.petListings.isEmpty()) {
                    item {
                        Text(
                            text = "You don't have pets for adoption",
                            style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.tertiary),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(state.petListings) { petInfo ->
                        Spacer(modifier = Modifier.height(20.dp))
                        MyPetItem(context = context, petInfo = petInfo, onDeletePost = {
                            myPetsModel.deletePost(it)
                            myPetsModel.getMyPets()
                        }, onUpdatePost = {
                            navigator.navigateToAddPostScreen(it)
                        }) {
                            navigator.navigateToPetDetailScreen(petInfo.petId)
                        }
                    }
                }
            }
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            }
        }
    }
}





