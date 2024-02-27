package com.home.ui.screens.pet_details.itmes

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.core.common.R
import com.core.common.ui.theme.Beige
import com.home.ui.screens.pet_details.PetDetailState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageDetailItem(
    modifier: Modifier = Modifier,
    context: Context,
    petInfo: PetDetailState,
    onLike: (Boolean) -> Unit
) {

    var isLiked by remember {
        mutableStateOf(petInfo.petFavorite)
    }

    val pagerState = rememberPagerState(pageCount = {
        petInfo.petPhotos.size
    })

    LaunchedEffect(key1 = petInfo.petFavorite) {
        isLiked = petInfo.petFavorite
    }


    Box(modifier = Modifier.fillMaxWidth()) {

        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { index ->
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context = context)
                    .data(
                        petInfo.petPhotos[index]
                    )
                    .crossfade(1000)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_pet)
            )

            Image(
                painter = painter,
                contentDescription = "pet detail image",
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .height(350.dp),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.TopStart)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.tertiary)
        ) {
            repeat(pagerState.pageCount) { iteration ->
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .padding(5.dp)
                        .clip(CircleShape)
                        .background(
                            if (pagerState.currentPage == iteration)
                                Beige
                            else
                                Beige.copy(alpha = 0.3f)
                        )
                )
            }
        }

        IconButton(
            onClick = {
                if (isLiked) {
                    onLike(true)
                } else onLike(false)

                isLiked = !isLiked
            }, modifier = Modifier
                .align(
                    Alignment.TopEnd
                )
        ) {
            Icon(
                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(40.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .background(Color.White.copy(0.4f))
                .padding(5.dp)
                .padding(horizontal = 15.dp)
        ) {

            Text(
                text = petInfo.petName,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
            )
            Row {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "location",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = petInfo.address ?: "",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal)
                )
            }
        }
    }
}
