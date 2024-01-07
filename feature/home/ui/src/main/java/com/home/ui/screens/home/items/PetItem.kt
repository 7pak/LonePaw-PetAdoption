package com.home.ui.screens.home.items

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.core.common.R
import com.core.common.ui.theme.Red
import com.core.database.model.PetInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetItem(context: Context, pet: PetInfo, onLike: (Boolean) -> Unit, onNavigate: () -> Unit) {
    Card(
        onClick = {
            onNavigate()
        }, modifier = Modifier
            .height(150.dp)
            .padding(horizontal = 20.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(15.dp)
            )
            .clip(RoundedCornerShape(15.dp))
    ) {
        PetData(context, pet) {
            onLike(it)
        }
    }
}


@Composable
fun PetData(context: Context, pet: PetInfo, onLike: (Boolean) -> Unit) {

    var isLiked by remember {
        mutableStateOf(pet.petFavorite)
    }

    LaunchedEffect(key1 = pet.petFavorite){
        isLiked = pet.petFavorite
    }


    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context = context)
                .data(
                    pet.petPhoto
                )
                .crossfade(500)
                .build(),
            placeholder = painterResource(id = R.drawable.ic_pet)
        )

        Image(
            painter = painter,
            contentDescription = "pet image",
            modifier = Modifier
                .size(150.dp)
                .weight(4f)
                .clip(shape = RoundedCornerShape(15.dp))
                .border(width = 3.dp, shape = RoundedCornerShape(15.dp), color = Red),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .weight(4f)
        ) {
            Text(
                text = "${pet.petName}",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${pet.petBreed}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis


            )

            Text(
                text = "${pet.petAge}",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.ExtraLight),
                modifier = Modifier.padding(vertical = 5.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis

            )

            Row(
                modifier = Modifier.padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "location",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "${pet.address}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 5.dp)
                )
            }
        }

        IconButton(onClick = {
            if (isLiked) {
                onLike(true)
            }else onLike(false)
        }, modifier = Modifier.weight(1f)) {
            Icon(
                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "favorite",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PetItemPreview() {
    // PetItem(LocalContext.current)
}