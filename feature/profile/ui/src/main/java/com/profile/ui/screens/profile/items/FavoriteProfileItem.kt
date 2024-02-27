package com.profile.ui.screens.profile.items

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.core.common.R
import com.core.database.model.PetInfo

@Composable
fun FavoriteProfileItem(context:Context,modifier: Modifier = Modifier,petInfo: PetInfo?,onNavigate:()->Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth()
            .clickable {
                onNavigate()
            }
    ) {
        Text(
            text = "My Favorite",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic
            )
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.tertiary.copy(0.4f))
                .padding(6.dp)
        ) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context = context)
                    .data(
                        petInfo?.petPhoto?.firstOrNull()?.ifEmpty {R.drawable.ic_pet }?:R.drawable.ic_pet
                    )
                    .transformations(CircleCropTransformation())
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_pet)
            )
            Image(
                painter = painter,
                contentDescription = "pet favorite",
                modifier = Modifier
                    .size(30.dp)
                    .alpha(0.4f)
            )
            Text(
                text = petInfo?.petName?.ifEmpty { "Pet Name"}?:"Pet Name",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif
                ),
                modifier = Modifier.alpha(0.4f)
            )
        }
        Text(
            text = "Click to see more ..",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Thin,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier.padding(vertical = 10.dp)
        )
    }
}
