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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.common.R
import com.core.common.test_tags.HomeTestTags
import com.home.ui.screens.home.items.CategoryItem
import com.home.ui.screens.home.items.HeaderItem
import com.home.ui.screens.home.items.PetItem
import com.home.ui.screens.home.items.SearchItem
import com.home.ui.shared.HomeScreenNavigator
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@OptIn(ExperimentalMaterialApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    homeModel: HomeModel = hiltViewModel(),
    navigator: HomeScreenNavigator
) {
    val state = homeModel.state
    val context = LocalContext.current

    val hasUnseenMessage by homeModel.hasUnseenMessage.collectAsState()

    val pulRefreshState = rememberPullRefreshState(
        refreshing = homeModel.state.isRefreshing,
        onRefresh = {
            homeModel.getPetsInfos()
        }
    )

    LaunchedEffect(key1 = true) {
        if (state.selectedCategory.isEmpty() && state.searchQuery.isEmpty()) {
            homeModel.getPetsInfos()
        }
        homeModel.getProfile()
        homeModel.updateHasUnseenMessage()
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
                .testTag(HomeTestTags.HOME_SCREEN),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            HeaderItem(
                context = context,
                profilePic = state.profilePic,
                profileName = state.profileName,
                profileCountry = state.profileCountry,
                hasUnseenMessage = hasUnseenMessage,
                onChatNavigate = {
                    navigator.navigateToContactScreen()
                }
            ) {
                navigator.navigateToProfileScreen()
            }

            Box(modifier = Modifier.fillMaxSize()) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .pullRefresh(state = pulRefreshState)
                        .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                ) {
                    item {
                        Spacer(modifier = Modifier.height(40.dp))

                        SearchItem(homeModel = homeModel) {
                            homeModel.getSearchResult(state.searchQuery)
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        CategoryItem(homeModel = homeModel) {
                            homeModel.getPostsCategories()
                        }
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
                    if (state.petsListing.isNullOrEmpty()) {
                        item {
                            Text(
                                text = "There is no pet for adoption",
                                style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.tertiary)
                            )
                        }
                    } else {
                        items(state.petsListing) { pet ->
                            PetItem(context = context, pet = pet, onLike = {
                                if (it) {
                                    homeModel.removeFavorite(pet.petId)
                                } else {
                                    homeModel.addFavorite(pet.petId)
                                }
                            }) {
                                navigator.navigateToPetDetailScreen(pet.petId)
                            }

                            Spacer(modifier = Modifier.height(50.dp))
                        }
                    }
                }

                PullRefreshIndicator(
                    refreshing = state.isRefreshing,
                    state = pulRefreshState,
                    modifier = Modifier.align(
                        Alignment.TopCenter
                    )
                )

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
}

