package com.home.ui.screens.pet_details.itmes

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.core.common.R
import com.core.database.model.PetInfo
import com.home.ui.screens.pet_details.PetDetailState
import com.home.ui.ui.theme.PetAdoptionTheme
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PetDetailsItem(
    modifier: Modifier = Modifier,
    context: Context,
    petInfo: PetDetailState,
    onAdopt: (phone: String) -> Unit
) {
    val properties = mapOf(
        "Type" to petInfo.petType,
        "Breed" to petInfo.petBreed,
        "Age" to petInfo.petAge.toString(),
        "Gender" to petInfo.petGender
    )

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
            modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
            // .background(MaterialTheme.colorScheme.primary)
        ) {
            PetCharacteristics(modifier = Modifier.weight(4f), map = properties)
            PetOwnerItem(
                modifier = Modifier.weight(2f),
                context = context,
                ownerImage = petInfo.petPhoto,
                ownerName = petInfo.ownerName,
                createdAt = petInfo.createdAt
            )
            PetDesItem(modifier = Modifier.weight(4f), petDesc = petInfo.petDesc)
            PetContactInfoItem(modifier = Modifier.weight(3f)) {
                onAdopt(petInfo.contactNumber)
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PetCharacteristics(modifier: Modifier = Modifier, map: Map<String, String>) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            maxItemsInEachRow = 3,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            map.forEach { (characteristic, value) ->
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(100.dp)
                        )
                        .padding(10.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Black,
                                    fontFamily = FontFamily.Serif
                                )
                            ) {
                                append("$characteristic: ")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.tertiary,
                                    fontFamily = FontFamily.Monospace
                                )
                            ) {
                                append(value)
                            }
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Light,
                            fontFamily = FontFamily.Serif
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
        }
    }
}

@Composable
fun PetOwnerItem(
    modifier: Modifier = Modifier,
    context: Context,
    ownerImage: String,
    ownerName: String,
    createdAt: String
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context = context)
                .data(
                    R.drawable.ic_profile
                )
                .transformations(CircleCropTransformation())
                .build(),
            placeholder = painterResource(id = R.drawable.ic_profile),
        )

        Image(
            painter = painter,
            contentDescription = "profile_header",
            modifier = Modifier
                .size(60.dp)
                .weight(3f)
                .clickable { }, colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.tertiary)
        )

        Column(
            Modifier
                .padding(horizontal = 8.dp)
                .weight(10f)
        ) {
            Text(
                text = ownerName,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = "Owner",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light)
            )
        }

        Text(
            text = formatTimestamp(createdAt),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier.weight(4f)
        )
    }
}

@Composable
fun PetDesItem(modifier: Modifier = Modifier, petDesc: String) {
    Row(
        modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = petDesc,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Normal)
        )
    }
}

@Composable
fun PetContactInfoItem(modifier: Modifier = Modifier, onAdopt: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            //.background(Color.White)
            .padding(vertical = 20.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Call,
            contentDescription = "call",
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.weight(3f)
        )

        Icon(
            imageVector = Icons.Default.Message,
            contentDescription = "message",
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.weight(3f)
        )

        Button(modifier = Modifier
            .weight(10f)
            .height(50.dp), shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = Color.White
            ),
            onClick = {
                onAdopt()
            }) {
            Text(text = "Adopt Now")
        }

    }
}

fun formatTimestamp(timestamp: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())

    try {
        val date = inputFormat.parse(timestamp)
        return outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return ""
}

@Preview(showBackground = true)
@Composable
fun PetDetailItemPreview() {
    PetAdoptionTheme {
        //PetDetailsItem(context = LocalContext.current)
    }
}
