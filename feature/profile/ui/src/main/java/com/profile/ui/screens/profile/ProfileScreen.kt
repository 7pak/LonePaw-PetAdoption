package com.profile.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.common.R
import com.profile.ui.screens.profile.items.FavoriteProfileItem
import com.profile.ui.screens.profile.items.MyPetsProfileItem
import com.profile.ui.screens.profile.items.ProfileHeaderItem
import com.profile.ui.shared.ProfileScreenNavigator
import com.profile.ui.ui.theme.PetAdoptionTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@Destination
@Composable
fun ProfileScreen(navigator: ProfileScreenNavigator,profileModel: ProfileModel = hiltViewModel()) {

    val state = profileModel.state

    LaunchedEffect(key1 = true){
        profileModel.getProfile()
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        Image(
            painter = painterResource(id = R.drawable.background_post),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )

        Column(Modifier.fillMaxSize()) {
            val context = LocalContext.current
            ProfileHeaderItem(context = context,profileData = state, onLogout = {
                profileModel.logout()
                navigator.logout()
            }) {
                navigator.navigateToProfileDetailScreen()
            }

            Spacer(modifier = Modifier.height(60.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MyPetsProfileItem(context = context, modifier = Modifier.weight(4f),petInfo = profileModel.lastMyPet) {
                    navigator.navigateToMyPetsScreen()
                }
                Divider(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                )

                FavoriteProfileItem(context = context, modifier = Modifier.weight(4f),petInfo = profileModel.lastFavorite) {
                    navigator.navigateToFavoriteScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    PetAdoptionTheme {
        // ProfileScreen()
    }
}