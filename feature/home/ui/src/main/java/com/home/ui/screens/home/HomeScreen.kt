package com.home.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.common.R
import com.core.common.TestTags
import com.home.ui.shared.HomeScreenNavigator
import com.home.ui.screens.home.items.CategoryItem
import com.home.ui.screens.home.items.HeaderItem
import com.home.ui.screens.home.items.PetItem
import com.home.ui.screens.home.items.SearchItem
import com.home.ui.ui.theme.PetAdoptionTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    homeModel: HomeModel = hiltViewModel(),
    navigator: HomeScreenNavigator
) {
    val state = homeModel.state
    val context = LocalContext.current


    LaunchedEffect(key1 = true) {
        homeModel.getPetsInfos(true)
        homeModel.getProfile()
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                // .background(Color.White)
                .testTag(TestTags.HOME_SCREEN),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            HeaderItem(context = context,profilePic = state.profilePic,profileName = state.profileName,profileCountry = state.profileCountry) {
                navigator.navigateToProfileScreen()
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                  //  .background(MaterialTheme.colorScheme.primary)
            ) {
                item {
                    Spacer(modifier = Modifier.height(40.dp))

                    SearchItem(homeModel = homeModel)

                    Spacer(modifier = Modifier.height(30.dp))

                    CategoryItem()
                }

                item {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 30.dp, horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Newest Pets",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontStyle = FontStyle.Italic
                            )
                        )
                    }
                }
                state.petsListing?.let {
                    items(state.petsListing) { pet ->
                        PetItem(context = context, pet = pet, onLike = {
                            if (it){
                                homeModel.removeFavorite(pet.petId)
                                homeModel.getPetsInfos()
                            }else {
                                homeModel.addFavorite(pet.petId)
                                homeModel.getPetsInfos()
                            }
                        }) {
                            navigator.navigateToPetDetailScreen(pet.petId)
                        }

                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PetAdoptionTheme {
        // HomeScreen()
    }
}
