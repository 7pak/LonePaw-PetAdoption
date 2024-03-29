package com.profile.ui.screens.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.core.common.test_tags.HomeTestTags
import com.profile.ui.screens.favorite.items.FavoriteHeaderItem
import com.profile.ui.screens.favorite.items.FavoriteItem
import com.profile.ui.shared.ProfileScreenNavigator
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun FavoriteScreen(
    favoriteModel: FavoriteModel = hiltViewModel(),
    navigator: ProfileScreenNavigator
) {
    val context = LocalContext.current

    val state = favoriteModel.state
    LaunchedEffect(key1 =true ){
        favoriteModel.getFavorites()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .testTag(HomeTestTags.HOME_SCREEN),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ){

        FavoriteHeaderItem()
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(MaterialTheme.colorScheme.primary),
        ) {
            if (state.petListings.isNullOrEmpty()) {
                item {
                    Text(
                        text = "You didn't like any pet ):",
                        style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.tertiary),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(state.petListings) { petInfo ->
                    Spacer(modifier = Modifier.height(20.dp))
                    FavoriteItem(context = context, petInfo = petInfo, onRemove = {
                        favoriteModel.removeFavorite(petInfo.petId)
                    }) {
                        navigator.navigateToPetDetailScreen(petInfo.petId)
                    }
                }
            }
        }
    }
}
