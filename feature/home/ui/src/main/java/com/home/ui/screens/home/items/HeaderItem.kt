package com.home.ui.screens.home.items

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ForwardToInbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.core.common.R
import com.core.common.ui.theme.Beige
import com.home.ui.ui.theme.PetAdoptionTheme

@Composable
fun HeaderItem(
    context: Context,
    modifier: Modifier = Modifier,
    profilePic: String,
    profileName: String,
    profileCountry:String,
    onNavigate: () -> Unit
) {

    Row(
        modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context = context)
                .data(
                    profilePic.ifEmpty { R.drawable.ic_profile}
                )
                .transformations(CircleCropTransformation())
                .build(),
            placeholder = painterResource(id = R.drawable.ic_profile)
        )

        Image(
            painter = painter,
            contentDescription = "profile_header",
            modifier = Modifier
                .size(70.dp)
                // .weight(1f)
                .clickable {
                    onNavigate()
                }
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = profileName,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = Beige,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = profileCountry,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Beige,
                    fontWeight = FontWeight.Light
                )
            )


        }

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.ForwardToInbox,
                contentDescription = "inbox",
                tint = Beige,
                modifier = Modifier
                    .size(35.dp)
                    .weight(10f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderItemPreview() {
    PetAdoptionTheme {
        //HeaderItem(LocalContext.current) {}
    }
}