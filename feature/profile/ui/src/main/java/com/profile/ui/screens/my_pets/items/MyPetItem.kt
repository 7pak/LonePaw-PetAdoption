package com.profile.ui.screens.my_pets.items

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.core.common.R
import com.core.database.model.PetInfo
import com.profile.ui.ui.theme.PetAdoptionTheme

@Composable
fun MyPetItem(
    context: Context,
    petInfo: PetInfo,
    onDeletePost: (Int) -> Unit,
    onUpdatePost: (Int) -> Unit,
    onNavigate: () -> Unit
) {


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 5.dp)
            .clip(shape = CircleShape)
            .background(MaterialTheme.colorScheme.secondary.copy(0.5f))
            .padding(vertical = 8.dp)
            .clickable {
                onNavigate()
            }
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context = context)
                .data(
                    petInfo.petPhoto?.firstOrNull()?.ifEmpty {  R.drawable.ic_pet} ?: R.drawable.ic_pet
                )
                .transformations(CircleCropTransformation())
                .build(),
            placeholder = painterResource(id = R.drawable.ic_pet)
        )
        Image(
            painter = painter,
            contentDescription = "my pets",
            modifier = Modifier
                .size(65.dp)
                .weight(2.2f)
        )
        Text(
            text = petInfo.petName,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            ),
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(5f)
        )
        Row(
            Modifier
                .weight(4f)
                .padding(horizontal = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {
                    onUpdatePost(petInfo.petId)
                },
                modifier = Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(15.dp)
                )
            ) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "edit my pet")
            }

            Spacer(modifier = Modifier.width(10.dp))
            IconButton(
                onClick = {
                    onDeletePost(petInfo.petId)
                },
                modifier = Modifier.border(
                    width = 1.dp,
                    color = Color.Red,
                    shape = RoundedCornerShape(15.dp)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "edit my pet",
                    tint = Color.Red
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPetItemPreview() {
    PetAdoptionTheme {
        // MyPetItem(LocalContext.current)
    }
}