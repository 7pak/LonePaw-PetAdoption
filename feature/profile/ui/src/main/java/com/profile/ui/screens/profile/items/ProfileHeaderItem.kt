package com.profile.ui.screens.profile.items

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.core.common.R
import com.core.common.ui.theme.Beige
import com.core.network.profile_api.model.ProfileData
import com.profile.ui.screens.profile.ProfileState
import com.profile.ui.ui.theme.PetAdoptionTheme

@Composable
fun ProfileHeaderItem(context: Context,profileData: ProfileState,onLogout:()->Unit,onNavigate:()->Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = onLogout, modifier = Modifier.align(Alignment.TopStart)) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = "logout",
                modifier = Modifier.size(40.dp),
                tint = Beige
            )
        }

        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.TopEnd)) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "notification",
                modifier = Modifier.size(40.dp),
                tint = Beige
            )
        }

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context = context)
                .data(
                    profileData.profilePic.ifEmpty { R.drawable.ic_profile}
                )
                .transformations(CircleCropTransformation())
                .build(),
            placeholder = painterResource(id = R.drawable.ic_profile)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(top = 100.dp).clickable {
                    onNavigate()
                }
        ) {
            Image(
                painter = painter,
                contentDescription = "profile_header",
                modifier = Modifier
                    .size(110.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = CircleShape
                    )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = profileData.profileName)
            Text(text = "click to update info", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Light))
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ProfileHeaderItemPreview() {
    PetAdoptionTheme {
        ProfileHeaderItem(LocalContext.current, ProfileState(),{}){}
    }
}